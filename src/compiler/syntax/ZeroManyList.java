
package compiler.syntax;

public abstract class ZeroManyList<T extends SyntaxElement> extends List<T> {
    public ZeroManyList(Class<T> _class) {
        super(_class);
    }
}