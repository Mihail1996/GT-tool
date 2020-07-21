package ist.yasat.model;

import ist.yasat.expressionVisitor.Visitor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ReturnStatement implements Statement {
    private Expression expression;

    @Override
    public boolean accept(Visitor v) {
        return v.visit(this);
    }
}
