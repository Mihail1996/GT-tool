package ist.yasat.expressionVisitor;

import ist.yasat.model.*;

public interface Visitor {

    void visit(ArithmeticOperation e);
    void visit(FunctionCall e);
    void visit(Expression expression);
    void visit(Assignment expression);
    void visit(ReturnStatement returnStatement);
}
