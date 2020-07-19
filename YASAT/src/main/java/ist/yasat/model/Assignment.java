package ist.yasat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assignment implements Statement {
    private Variable variable;
    private Expression value;
}
