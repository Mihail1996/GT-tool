package ist.yasat.model;

import ist.yasat.expressionVisitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FunctionCall extends Expression {
    @NonNull
    private String functionName;

    @Override
    public boolean accept(Visitor v) {
        return v.visit(this);
    }
}
