package ist.yasat.expressionVisitor;

import ist.yasat.model.*;

import java.util.List;

public class TaintVisitor implements Visitor {

    @Override
    public boolean visit(ArithmeticOperation expression) {
        expression.setTainted(propagateTaintInList(expression.getMembers()));
        return expression.isTainted();

    }

    @Override
    public boolean visit(FunctionCall expression) {
        expression.setTainted(propagateTaintInList(expression.getMembers()));
        return expression.isTainted();

    }

    @Override
    public boolean visit(Expression e) {
        e.setTainted(propagateTaintInList(e.getMembers()));
        return e.isTainted();
    }

    @Override
    public boolean visit(Assignment e) {
        var taintExpr = e.getValue().accept(this);
        e.setTainted(taintExpr);
        e.getVariable().setTainted(taintExpr);
        return e.isTainted();
    }

    @Override
    public boolean visit(ReturnStatement stmt) {
        return stmt.getExpression().accept(this);
    }

    @Override
    public boolean visit(Variable var) {
        return var.isTainted();
    }

    @Override
    public boolean visit(Constant constant) {
        return constant.isTainted();
    }

    public boolean propagateTaintInList(List<Expression> expressions) {
        boolean taint = false;
        for (Expression expr : expressions) {
            taint = taint || expr.accept(this);
        }
        return taint;
    }
}
