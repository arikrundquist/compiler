
package compiler;

import compiler.syntax.SyntaxElement;

public abstract class Language {

    private final java.util.HashSet<Class<? extends SyntaxElement>> elements = new java.util.HashSet<Class<? extends SyntaxElement>>();

    protected final void add(Class<? extends SyntaxElement> element) {
        this.elements.add(element);
    }

    public final <T extends SyntaxElement> Parser<T> parser(Class<T> target) {
        return new Parser<T>(target, elements);
    }

    public static void main(String[] args) {
        

    }
}
