
package compiler.syntax;

public abstract class Nullable<T extends SyntaxElement> extends SyntaxElement {
    private T value;
    public boolean hasValue() {
        return this.value != null;
    }
    public T value() {
        return this.value;
    }
    public static <T extends SyntaxElement> void set(Nullable<T> nullable, T value) {
        nullable.value = value;
    }
}
