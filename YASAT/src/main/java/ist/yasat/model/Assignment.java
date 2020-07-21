package ist.yasat.model;

import ist.yasat.expressionVisitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assignment extends Expression {
    private Variable variable;
    private Expression value;

    @Override
    public boolean accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
