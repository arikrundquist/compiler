
package compiler;

import compiler.syntax.*;

public final class Parser<T extends SyntaxElement> {
    
    protected Parser(Class<T> target, java.util.HashSet<Class<? extends SyntaxElement>> elements) {
    }

    public T parse(String text) {
        return null;
    }
}
