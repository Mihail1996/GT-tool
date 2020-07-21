package ist.yasat.expressionVisitor;

import ist.yasat.model.*;

public interface Visitor {

    boolean visit(ArithmeticOperation e);
    boolean visit(FunctionCall e);
    boolean visit(Expression e);
    boolean visit(Assignment e);
    boolean visit(ReturnStatement stmt);
    boolean visit(Variable var);
    boolean visit(Constant constant);
}
