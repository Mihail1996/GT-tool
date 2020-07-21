package ist.yasat.model;

import ist.yasat.expressionVisitor.Visitor;

public interface Statement {
    boolean accept(Visitor v);
}
