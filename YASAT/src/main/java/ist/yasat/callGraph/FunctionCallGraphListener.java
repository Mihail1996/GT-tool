package ist.yasat.callGraph;

import ist.yasat.expressionVisitor.ExpressionVisitor;
import ist.yasat.model.*;
import ist.yasat.parser.PhpParser;
import ist.yasat.parser.PhpParserBaseListener;
import ist.yasat.settings.Settings;
import lombok.Getter;
import org.antlr.v4.misc.OrderedHashMap;

import java.util.Stack;


public class FunctionCallGraphListener extends PhpParserBaseListener {
    private Settings settings;

    @Getter
    private final OrderedHashMap<String, Function> functions = new OrderedHashMap<>();

    private final static Function ROOT_FUNCTION = new Function("ROOT_FUNCTION");

    private final Stack<Expression> expressions = new Stack<>();

    private Function currentFunction = null;

    public FunctionCallGraphListener(Settings settings) {
        this.settings = settings;
        functions.put(ROOT_FUNCTION.getFunctionName(), ROOT_FUNCTION);
    }

    private void processExpression(Expression expression) {
        if (!expressions.empty())
            expressions.peek().accept(new ExpressionVisitor(expression));
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
        expressions.push(funcCall);
    }

    @Override
    public void exitFunctionCall(PhpParser.FunctionCallContext ctx) {
        expressions.pop();
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
        expressions.push(assignment);
    }

    @Override
    public void exitAssignmentExpression(PhpParser.AssignmentExpressionContext ctx) {
        expressions.pop();
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
        var expr = new ArithmeticOperation();
        processExpression(expr);
        expressions.push(expr);
    }

    @Override
    public void exitArithmeticExpression(PhpParser.ArithmeticExpressionContext ctx) {
        expressions.pop();
    }

    @Override
    public void enterChainExpression(PhpParser.ChainExpressionContext ctx) {
        var expr = new Expression();
        processExpression(expr);
        expressions.push(expr);
    }

    @Override
    public void exitChainExpression(PhpParser.ChainExpressionContext ctx) {
        expressions.pop();
    }

}
