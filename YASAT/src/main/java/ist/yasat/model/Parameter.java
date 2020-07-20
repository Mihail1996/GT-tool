package ist.yasat.model;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Parameter extends Taintable {
    @NonNull
    private String name;
    private String type;
}
