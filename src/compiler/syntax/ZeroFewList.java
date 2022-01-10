
package compiler.syntax;

public abstract class ZeroFewList<T extends SyntaxElement> extends List<T> {
    public ZeroFewList(Class<T> _class) {
        super(_class);
    }
}
