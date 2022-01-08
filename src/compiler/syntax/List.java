
package compiler.syntax;

public abstract class List<T extends SyntaxElement> extends SyntaxElement {
    public final java.util.List<T> list = new java.util.ArrayList<T>();
}
