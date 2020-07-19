package ist.yasat.callGraph;

import ist.yasat.model.*;
import ist.yasat.parser.PhpParser;
import ist.yasat.parser.PhpParserBaseListener;
import ist.yasat.settings.Settings;
import lombok.Getter;
import org.antlr.v4.misc.OrderedHashMap;


public class FunctionCallGraphListener extends PhpParserBaseListener {

    @Getter
    private final OrderedHashMap<String, Function> functions = new OrderedHashMap<>();

    private final static Function ROOT_FUNCTION = new Function("ROOT_FUNCTION");
    private Function currentFunction = null;
    private Settings settings;
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
        var func = currentFunction != null ? currentFunction : ROOT_FUNCTION;
        try {
            func.getFunctionCalls().add(new FunctionCall(ctx.functionCallName().qualifiedNamespaceName().namespaceNameList().identifier(0).getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (currentAssignment != null)
            currentAssignment.setVariable(var);
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

}
