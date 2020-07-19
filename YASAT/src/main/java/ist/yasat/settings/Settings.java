package ist.yasat.settings;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Settings {
    private Map<String, List<String>> taintSettings;
}
