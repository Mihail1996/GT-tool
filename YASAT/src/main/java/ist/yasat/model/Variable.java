package ist.yasat.model;

import ist.yasat.expressionVisitor.Visitor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Variable extends Expression {
    @NonNull
    private String name;
    private String type;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
