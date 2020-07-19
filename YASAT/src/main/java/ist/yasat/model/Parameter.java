package ist.yasat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Parameter extends Taintable {
    private String name;
    private String type;
}
