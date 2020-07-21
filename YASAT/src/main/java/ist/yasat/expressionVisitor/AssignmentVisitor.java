package ist.yasat.expressionVisitor;

import ist.yasat.model.*;

public class AssignmentVisitor implements Visitor {
    private Assignment assignment;

    public AssignmentVisitor(Assignment assignment) {
        this.assignment = assignment;
    }

    @Override
    public boolean visit(ArithmeticOperation e) {
        assignment.setValue(e);
        return true;
    }

    @Override
    public boolean visit(FunctionCall e) {
        assignment.setValue(e);
        return true;
    }

    @Override
    public boolean visit(Expression e) {
        assignment.setValue(e);
        return true;
    }

    @Override
    public boolean visit(Assignment e) {
        return false;
    }

    @Override
    public boolean visit(ReturnStatement stmt) {
        return false;
    }

    @Override
    public boolean visit(Variable var) {
        assignment.setVariable(var);
        return true;
    }

    @Override
    public boolean visit(Constant constant) {
        assignment.setValue(constant);
        return false;
    }
}
