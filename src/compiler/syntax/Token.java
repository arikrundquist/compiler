
package compiler.syntax;

public abstract class Token extends SyntaxElement {

    public String value;

    private final String regex;
    public Token(String regex) {
        this.regex = regex;
    }

    public static String regex(Token token) {
        return token.regex;
    }
}
