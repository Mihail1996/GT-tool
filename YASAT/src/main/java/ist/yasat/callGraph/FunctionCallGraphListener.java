package ist.yasat.callGraph;

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
    private Stack<FunctionCall> functionCallsStack = new Stack<>();
    private Stack<Expression> expressionsStack = new Stack<>();


    private Function currentFunction = null;
    private Assignment currentAssignment = null;

    public FunctionCallGraphListener(Settings settings) {
        this.settings = settings;
        functions.put(ROOT_FUNCTION.getFunctionName(), ROOT_FUNCTION);
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
        if (currentAssignment != null && functionCallsStack.empty())
            currentAssignment.setValue(funcCall);
        else if (!functionCallsStack.empty())
            functionCallsStack.peek().getArguments().add(funcCall);
        functionCallsStack.push(funcCall);
    }

    @Override
    public void exitFunctionCall(PhpParser.FunctionCallContext ctx) {
        functionCallsStack.pop();
    }

    @Override
    public void enterFormalParameter(PhpParser.FormalParameterContext ctx) {
        String paramName = ctx.variableInitializer().VarName().getText();
        functions.get(currentFunction.getFunctionName()).getParameters().put(paramName, new Parameter(null, paramName));
    }

    @Override
    public void enterKeyedVariable(PhpParser.KeyedVariableContext ctx) {
        var var = new Variable(ctx.VarName().getText());
        functions.get(currentFunction.getFunctionName()).getVariables().put(var.getName(), var);
        if (currentAssignment != null && expressionsStack.empty())
            currentAssignment.setVariable(var);
        else if (!expressionsStack.empty())
            expressionsStack.peek().getMembers().add(var);
        else if (!functionCallsStack.empty())
            functionCallsStack.peek().getArguments().add(var);
    }

    @Override
    public void enterAssignmentExpression(PhpParser.AssignmentExpressionContext ctx) {
        currentAssignment = new Assignment();
        currentFunction.getStatements().add(currentAssignment);
    }

    @Override
    public void exitAssignmentExpression(PhpParser.AssignmentExpressionContext ctx) {
        currentAssignment = null;
    }

    @Override
    public void enterStringConstant(PhpParser.StringConstantContext ctx) {
        if (!functionCallsStack.empty())
            functionCallsStack.peek().getArguments().add(new Constant(ctx.getText()));
    }

    @Override
    public void enterInterpolatedStringPart(PhpParser.InterpolatedStringPartContext ctx) {
        var constantString = new Constant(ctx.getText());
        if (!expressionsStack.empty())
            expressionsStack.peek().getMembers().add(constantString);
        else if (!functionCallsStack.empty())
            functionCallsStack.peek().getArguments().add(constantString);
    }

    @Override
    public void enterLiteralConstant(PhpParser.LiteralConstantContext ctx) {
        var constant = new Constant(ctx.getText());
        if (!expressionsStack.empty())
            expressionsStack.peek().getMembers().add(constant);
        else if (!functionCallsStack.empty())
            functionCallsStack.peek().getArguments().add(constant);
        else if (currentAssignment != null)
            currentAssignment.setValue(constant);
    }

    @Override
    public void enterArithmeticExpression(PhpParser.ArithmeticExpressionContext ctx) {
        var expr = new Expression();
        if (currentAssignment != null && expressionsStack.empty())
            currentAssignment.setValue(expr);
        else if (!functionCallsStack.empty())
            functionCallsStack.peek().getArguments().add(expr);
        expressionsStack.push(expr);
    }

    @Override
    public void exitArithmeticExpression(PhpParser.ArithmeticExpressionContext ctx) {
        expressionsStack.pop();
    }

    @Override
    public void enterChainExpression(PhpParser.ChainExpressionContext ctx) {
        var expr = new Expression();
        if (currentAssignment != null && expressionsStack.empty())
            currentAssignment.setValue(expr);
        else if (!functionCallsStack.empty())
            functionCallsStack.peek().getArguments().add(expr);
        expressionsStack.push(expr);
    }

    @Override
    public void exitChainExpression(PhpParser.ChainExpressionContext ctx) {
        expressionsStack.pop();

    }

}
