
package compiler.syntax;

public abstract class List<T extends SyntaxElement> extends SyntaxElement {
    public final java.util.List<T> list = new java.util.ArrayList<T>();
    private final Class<T> _class;
    protected List(Class<T> _class) {
        this._class = _class;
    }
    public static <T extends SyntaxElement> Class<T> getClass(List<T> list) {
        return list._class;
    }
}
