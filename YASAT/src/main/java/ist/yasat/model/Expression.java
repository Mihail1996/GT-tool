package ist.yasat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expression extends Taintable implements Statement {
    private List<Variable> variables = new ArrayList<>();
}
