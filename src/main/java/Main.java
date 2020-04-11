import org.antlr.v4.runtime.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        List<String> lines = new ArrayList<>();
        for (;;) {
            System.out.print(">");

            lines.add(reader.readLine());
            ChocoPyLexer lexer = new ChocoPyLexer(
                    CharStreams.fromString(lines.stream().collect(Collectors.joining("\n")))
            );

            List<? extends Token> tokens = lexer.getAllTokens();
            if (tokens.size() >2 &&
                tokens.get(tokens.size() - 2).getType() == ChocoPyLexer.COLON &&
                tokens.get(tokens.size() - 1).getType() == ChocoPyLexer.LINE_BREAK) {
                continue;
            }

            long indents = tokens.stream().mapToInt(Token::getType).filter(t -> t == ChocoPyLexer.INDENT).count();
            long dedents = tokens.stream().mapToInt(Token::getType).filter(t -> t == ChocoPyLexer.DEDENT).count();

            if (indents > dedents || tokens.get(tokens.size() -1 ).getType() == ChocoPyLexer.DEDENT) {
                continue;
            }

            final ChocoPyParser parser = new ChocoPyParser(
                    new CommonTokenStream(new ListTokenSource(tokens))
            );
            parser.setBuildParseTree(true);
            System.out.println(parser.program().toStringTree(parser));

            lines.clear();
        }
    }

    private static void run(String line) {

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
