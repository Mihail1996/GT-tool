package ist.yasat.model;

import ist.yasat.expressionVisitor.Visitor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArithmeticOperation extends Expression {
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
