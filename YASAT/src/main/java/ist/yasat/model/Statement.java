package ist.yasat.model;

import ist.yasat.expressionVisitor.Visitor;

public interface Statement {
    void accept(Visitor v);
}
