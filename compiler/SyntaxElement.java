
package compiler;

public abstract class SyntaxElement {

    private SyntaxElement() { }

    public final static class Token extends SyntaxElement {

        public String value;

        private final String regex;
        public Token(String regex) {
            this.regex = regex;
        }
    }
    public static final class Nullable<T extends SyntaxElement> extends SyntaxElement {
        private T value;
        public boolean hasValue() {
            return this.value != null;
        }
        public T value() {
            return this.value;
        }
    }
    public static abstract class List<T extends SyntaxElement> extends SyntaxElement {
        public final java.util.List<T> list = new java.util.ArrayList<T>();
        private List() { }
    }
    public static final class ZeroFewList<T extends SyntaxElement> extends List<T> { }
    public static final class ZeroManyList<T extends SyntaxElement> extends List<T> { }
    public static final class OneFewList<T extends SyntaxElement> extends List<T> { }
    public static final class OneManyList<T extends SyntaxElement> extends List<T> { }
}
