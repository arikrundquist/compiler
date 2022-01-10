
package compiler.syntax;

public abstract class OneManyList<T extends SyntaxElement> extends List<T> {
    public OneManyList(Class<T> _class) {
        super(_class);
    }
}
