package ist.yasat.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data@NoArgsConstructor
@RequiredArgsConstructor
public class Parameter extends Taintable {
    @NonNull
    private String name;
    private String type;
}
