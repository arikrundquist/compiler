
package compiler.syntax;

public abstract class OneFewList<T extends SyntaxElement> extends List<T> {
    public OneFewList(Class<T> _class) {
        super(_class);
    }
}
