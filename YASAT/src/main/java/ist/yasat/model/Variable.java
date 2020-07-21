package ist.yasat.model;

import ist.yasat.expressionVisitor.Visitor;
import lombok.*;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Variable extends Expression {
    @NonNull
    private String name;
    private String type;

    @Override
    public boolean accept(Visitor v) {
        return v.visit(this);
    }
}
