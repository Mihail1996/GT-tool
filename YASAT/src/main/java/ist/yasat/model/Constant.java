package ist.yasat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Constant extends Expression{
    public Object value;
}
