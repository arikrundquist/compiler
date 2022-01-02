
import java.util.ArrayList;
import java.util.Stack;
import java.io.Serializable;
import java.lang.reflect.Field;

public class Parser implements Serializable {

    private static class Tuple<T1, T2> {
        public final T1 left; public final T2 right;
        public Tuple(T1 left, T2 right) {
            this.left = left;
            this.right = right;
        }
    }

    public static class StateMachine implements Serializable {
        
        private StateMachine[] next = new StateMachine[256];
        private String accept;

        protected Tuple<String, StateMachine> enter(char c) {
            return new Tuple<String, StateMachine>(accept, next[c]);
        }
    }

    private final StateMachine statemachine;
    public Parser(StateMachine statemachine) {
        this.statemachine = statemachine;
    }

    public Object parse(String text) {
        StateMachine state = this.statemachine;
        Stack<Object> parseStack = new Stack<Object>();
        StringBuffer token = new StringBuffer();
        int index = 0;
        while(state != null && index < text.length()) {
            char c = text.charAt(index);
            Tuple<String, StateMachine> ret = state.enter(c);
            String accept = ret.left;
            state = ret.right;
            if(accept != null) {
                if(accept.equals("String")) {
                    parseStack.push(token.toString());
                    token = new StringBuffer();
                }else {
                    try {
                        Object o = Class.forName(accept).getConstructor().newInstance();
                        Field[] f = Interpreter.getSortedFields(o);
                        for(int i = f.length - 1; i >= 0; i--) {
                            f[i].set(o, parseStack.pop());
                        }
                        parseStack.push(o);
                    }catch(Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
            if(!(c == ' ' || c == '\n' || c == '\r')) {
                token.append(c);
            }
        }
        return null;
    }
}
