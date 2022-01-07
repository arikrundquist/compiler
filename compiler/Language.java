
package compiler;

public class Language {

    private final java.util.HashSet<Class<? extends SyntaxElement>> elements = new java.util.HashSet<Class<? extends SyntaxElement>>();

    public void add(Class<? extends SyntaxElement> element) {
        this.elements.add(element);
    }

    public <T extends SyntaxElement> Parser<T> parser(Class<T> target) {

        return null;
    }
}
