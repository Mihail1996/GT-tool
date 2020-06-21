package ist.yasat;

import ist.yasat.parser.PhpLexer;
import ist.yasat.parser.PhpParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class ParserRun {

    public static void main(String[] args) throws IOException {
        CharStream input = CharStreams.fromFileName("src/main/resources/example/example1.php");
        PhpLexer lexer = new PhpLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PhpParser parser = new PhpParser(tokens);
        ParseTree tree = parser.phpBlock();

        System.out.println("Success");
    }
}
