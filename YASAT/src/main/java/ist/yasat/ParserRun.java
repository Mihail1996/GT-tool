package ist.yasat;

import com.fasterxml.jackson.databind.ObjectMapper;
import ist.yasat.callGraph.FunctionCallGraphListener;
import ist.yasat.model.Expression;
import ist.yasat.model.Parameter;
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
        var listener = new FunctionCallGraphListener(settings);
        walker.walk(listener, tree);
        var graph = listener.getFunctions();
        var param = new Parameter("sd", "sdgsd");
        var expr = new Expression();
        expr.setTainted(true);
        System.out.println("Success");

    }
}
