package ist.yasat.callGraph;

import ist.yasat.expressionVisitor.ExpressionVisitor;
import ist.yasat.model.*;
import ist.yasat.parser.PhpParser;
import ist.yasat.parser.PhpParserBaseListener;
import lombok.Getter;
import org.antlr.v4.misc.OrderedHashMap;

import java.util.Stack;


public class FunctionCallGraphListener extends PhpParserBaseListener {

    @Getter
    private final OrderedHashMap<String, Function> functions = new OrderedHashMap<>();

    private final static Function ROOT_FUNCTION = new Function("ROOT_FUNCTION");

    private final Stack<Statement> exprsAndStmts = new Stack<>();

    private Function currentFunction = null;

    public FunctionCallGraphListener() {
        functions.put(ROOT_FUNCTION.getFunctionName(), ROOT_FUNCTION);
    }

    private void processExpression(Expression expression) {
        if (!exprsAndStmts.empty())
            exprsAndStmts.peek().accept(new ExpressionVisitor(expression));
    }

    @Override
    public void enterFunctionDeclaration(PhpParser.FunctionDeclarationContext ctx) {
        currentFunction = new Function(ctx.identifier().getText());
        functions.put(currentFunction.getFunctionName(), currentFunction);
    }

    @Override
    public void exitFunctionDeclaration(PhpParser.FunctionDeclarationContext ctx) {
        currentFunction = ROOT_FUNCTION;
    }

    @Override
    public void enterFunctionCall(PhpParser.FunctionCallContext ctx) {
        var funcCall = new FunctionCall(ctx.functionCallName().qualifiedNamespaceName().namespaceNameList().identifier(0).getText());
        currentFunction.getFunctionCalls().add(funcCall);
        processExpression(funcCall);
        exprsAndStmts.push(funcCall);
    }

    @Override
    public void exitFunctionCall(PhpParser.FunctionCallContext ctx) {
        exprsAndStmts.pop();
    }

    @Override
    public void enterFormalParameter(PhpParser.FormalParameterContext ctx) {
        String paramName = ctx.variableInitializer().VarName().getText();
        functions.get(currentFunction.getFunctionName()).getParameters().put(paramName, new Parameter(paramName));
    }

    @Override
    public void enterKeyedVariable(PhpParser.KeyedVariableContext ctx) {
        var var = new Variable(ctx.VarName().getText());
        functions.get(currentFunction.getFunctionName()).getVariables().put(var.getName(), var);
        processExpression(var);
    }

    @Override
    public void enterAssignmentExpression(PhpParser.AssignmentExpressionContext ctx) {
        var assignment = new Assignment();
        currentFunction.getStatements().add(assignment);
        exprsAndStmts.push(assignment);
    }

    @Override
    public void exitAssignmentExpression(PhpParser.AssignmentExpressionContext ctx) {
        exprsAndStmts.pop();
    }

    @Override
    public void enterInterpolatedStringPart(PhpParser.InterpolatedStringPartContext ctx) {
        processExpression(new Constant(ctx.getText()));
    }

    @Override
    public void enterLiteralConstant(PhpParser.LiteralConstantContext ctx) {
        processExpression(new Constant(ctx.getText()));
    }

    @Override
    public void enterArithmeticExpression(PhpParser.ArithmeticExpressionContext ctx) {
        processGenericExpression(ArithmeticOperation.class);

    }

    @Override
    public void exitArithmeticExpression(PhpParser.ArithmeticExpressionContext ctx) {
        exprsAndStmts.pop();
    }

    @Override
    public void enterChainExpression(PhpParser.ChainExpressionContext ctx) {
        processGenericExpression(Expression.class);
    }

    @Override
    public void exitChainExpression(PhpParser.ChainExpressionContext ctx) {
        exprsAndStmts.pop();
    }

    @Override
    public void enterReturnStatement(PhpParser.ReturnStatementContext ctx) {
        var stmt = new ReturnStatement();
        currentFunction.getStatements().add(stmt);
        exprsAndStmts.push(stmt);
    }

    @Override
    public void exitReturnStatement(PhpParser.ReturnStatementContext ctx) {
        exprsAndStmts.pop();
    }

    @Override
    public void enterScalarExpression(PhpParser.ScalarExpressionContext ctx) {
        processGenericExpression(Expression.class);
    }

    @Override
    public void exitScalarExpression(PhpParser.ScalarExpressionContext ctx) {
        exprsAndStmts.pop();
    }

    public <T extends Expression> void processGenericExpression(Class<T> klass) {
        try {
            T expr = klass.getDeclaredConstructor().newInstance();
            processExpression(expr);
            exprsAndStmts.push(expr);
        } catch (Exception e) {
            System.out.println("An error has occurred " + e.getMessage());
        }
    }


}
