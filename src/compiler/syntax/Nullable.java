
package compiler.syntax;

public abstract class Nullable<T extends SyntaxElement> extends SyntaxElement {
    private T value;
    private final Class<T> _class;
    protected Nullable(Class<T> _class) {
        this._class = _class;
    }
    public boolean hasValue() {
        return this.value != null;
    }
    public T value() {
        return this.value;
    }
    public static <T extends SyntaxElement> void set(Nullable<T> nullable, T value) {
        nullable.value = value;
    }
    public static <T extends SyntaxElement> Class<T> getClass(Nullable<T> nullable) {
        return nullable._class;
    }
}
