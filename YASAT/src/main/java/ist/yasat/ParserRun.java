package ist.yasat;

import com.fasterxml.jackson.databind.ObjectMapper;
import ist.yasat.callGraph.FunctionCallGraphListener;
import ist.yasat.expressionVisitor.AssignmentVisitor;
import ist.yasat.expressionVisitor.TaintVisitor;
import ist.yasat.model.*;
import ist.yasat.parser.PhpLexer;
import ist.yasat.parser.PhpParser;
import ist.yasat.settings.Settings;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.temporal.ValueRange;
import java.util.HashMap;

public class ParserRun {

    public static void main(String[] args) throws IOException {
        CharStream input = CharStreams.fromFileName("src/main/resources/example/example1.php");
        PhpLexer lexer = new PhpLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PhpParser parser = new PhpParser(tokens);
        ParseTree tree = parser.phpBlock();
        ParseTreeWalker walker = new ParseTreeWalker();
        ObjectMapper mapper = new ObjectMapper();
        var settings = mapper.readValue(new FileInputStream("src/main/resources/settings.json"), Settings.class);
        var listener = new FunctionCallGraphListener();
        walker.walk(listener, tree);
        var graph = listener.getFunctions();

        var taintVisitor = new TaintVisitor();
        var firstFunc = graph.get("first");
        firstFunc.getVariables().get("$id").setTainted(true);

        for (Statement stmt : firstFunc.getStatements()) {
            stmt.accept(taintVisitor);
        }
        System.out.println("Success");

    }
}
