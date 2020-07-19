package ist.yasat.model;

import lombok.Data;

@Data
public abstract class Taintable {
    private boolean isTainted = false;
}
