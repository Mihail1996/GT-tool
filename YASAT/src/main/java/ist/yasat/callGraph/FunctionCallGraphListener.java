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
    private Stack<FunctionCall> functionCalls = new Stack<>();


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
        if (currentAssignment != null && functionCalls.empty())
            currentAssignment.setValue(funcCall);
        if (!functionCalls.empty())
            functionCalls.peek().getArguments().add(funcCall);
        functionCalls.push(funcCall);
    }

    @Override
    public void exitFunctionCall(PhpParser.FunctionCallContext ctx) {
        functionCalls.pop();
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
        if (currentAssignment != null && functionCalls.empty())
            currentAssignment.setVariable(var);
        else if (!functionCalls.empty())
            functionCalls.peek().getArguments().add(var);
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
        if (!functionCalls.empty())
            functionCalls.peek().getArguments().add(new Constant(ctx.getText()));
    }

    @Override
    public void enterInterpolatedStringPart(PhpParser.InterpolatedStringPartContext ctx) {
        if (!functionCalls.empty())
            functionCalls.peek().getArguments().add(new Constant(ctx.getText()));
    }

    @Override
    public void enterLiteralConstant(PhpParser.LiteralConstantContext ctx) {
        var constant = new Constant(ctx.getText());
        if (!functionCalls.empty())
            functionCalls.peek().getArguments().add(constant);
        else if (currentAssignment != null)
            currentAssignment.setValue(constant);
    }

}
