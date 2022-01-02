
import java.util.ArrayList;
import java.util.Stack;
import java.io.Serializable;

public class Parser implements Serializable {

    public Parser() {

    }

    public Object parse(String text) {
        /*Stack<Builder<?>> parseStack = new Stack<Builder<?>>();

        if(parseStack.size() > 0) {
            parseStack.peek().build();
            if(parseStack.size() == 1) {
                return parseStack.pop().get();
            }
        }*/
        return null;
    }
/*
    private static abstract class Builder<T> {
        public final void build(Stack<Builder<?>> parseStack) {
            parseStack.pop();
            this._build(parseStack);
            parseStack.add(this);
        }
        private void _build(Stack<Builder<?>> parseStack) { }
        public Class<T> getClass();
        public T get();
    }
    private static class StringBuilder extends Builder<String> {
        private final StringBuffer sb = new StringBuffer();
        public StringBuilder() {
            
        }
        private void _build(Stack<Builder<?>> parseStack) {

        }
    }
    private static class NullBuilder*/
}
