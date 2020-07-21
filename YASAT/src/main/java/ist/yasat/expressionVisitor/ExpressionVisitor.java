package ist.yasat.expressionVisitor;

import ist.yasat.model.*;

public class ExpressionVisitor implements Visitor {
    private final Expression expr;

    public ExpressionVisitor(Expression expression) {
        expr = expression;
    }

    @Override
    public void visit(ArithmeticOperation e) {
        e.getMembers().add(expr);
    }

    @Override
    public void visit(FunctionCall e) {
        e.getArguments().add(expr);

    }

    @Override
    public void visit(Expression expression) {
        expression.getMembers().add(expr);
    }

    @Override
    public void visit(Assignment expression) {
        if (expression.getVariable() == null && (expr instanceof Variable))
            expression.setVariable((Variable) expr);
        else
            expression.setValue(expr);
    }

    @Override
    public void visit(ReturnStatement returnStatement) {
        returnStatement.setExpression(expr);
    }

}
