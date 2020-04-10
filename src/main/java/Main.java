import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: chocopy [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            run(reader.readLine());
        }
    }

    private static void run(String line) {
        ChocoPyLexer lexer = new ChocoPyLexer(CharStreams.fromString(line));
        final ChocoPyParser parser = new ChocoPyParser(
                new CommonTokenStream(lexer)
        );
        parser.setBuildParseTree(true);
        System.out.println(parser.program().toStringTree());
    }

    private static void runFile(String arg) throws IOException {
        ChocoPyLexer lexer = new ChocoPyLexer(CharStreams.fromPath(Path.of(arg)));
        final ChocoPyParser parser = new ChocoPyParser(
                new CommonTokenStream(lexer)
        );
        parser.setBuildParseTree(true);
        System.out.println(parser.program().toStringTree(parser));
    }
}
