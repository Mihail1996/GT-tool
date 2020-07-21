package ist.yasat.model;

import ist.yasat.expressionVisitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Constant extends Expression {
    public Object value;

    @Override
    public boolean accept(Visitor v) {
        return v.visit(this);
    }
}
