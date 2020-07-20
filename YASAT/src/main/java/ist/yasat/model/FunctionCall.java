package ist.yasat.model;

import ist.yasat.expressionVisitor.Visitor;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor

public class FunctionCall extends Expression {
    @NonNull
    private String functionName;
    private List<Expression> arguments = new ArrayList<>();

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
