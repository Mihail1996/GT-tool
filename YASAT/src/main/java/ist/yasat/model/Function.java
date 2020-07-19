package ist.yasat.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.misc.OrderedHashMap;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Function {

    @NonNull
    private String functionName;
    private OrderedHashMap<String, Parameter> parameters = new OrderedHashMap<>();
    private OrderedHashMap<String, Variable> variables = new OrderedHashMap<>();
    private ArrayList<FunctionCall> functionCalls = new ArrayList<>();
    private List<Statement> statements = new ArrayList<>();
}
