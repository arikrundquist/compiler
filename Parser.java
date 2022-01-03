
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Map.Entry;
import java.io.Serializable;
import java.lang.reflect.Field;

public class Parser implements Serializable {

    public static String nullableSuffix() {
        return "$$nullable";
    }
    public static String listSuffix() {
        return "$$list";
    }

    public static class StateMachineBuilder {
        private final Pointer<StateMachine> start = new Pointer<StateMachine>();
        public StateMachine compile() {
            return this.start.val;
        }
        public StateMachineBuilder branch(StateMachineBuilder next, StateMachineBuilder fail) {
            this.start.val = new BranchState(next.start, fail.start);
            return this;
        }
        public StateMachineBuilder sequence(ArrayList<StateMachineBuilder> builders) {
            ArrayList<Pointer<StateMachine>> states = new ArrayList<Pointer<StateMachine>>();
            for(StateMachineBuilder builder : builders) {
                states.add(builder.start);
            }
            this.start.val = new SequenceState(states);
            return this;
        }
        public StateMachineBuilder sequence(StateMachineBuilder... builders) {
            ArrayList<StateMachineBuilder> list = new ArrayList<StateMachineBuilder>();
            for(StateMachineBuilder builder : builders) {
                list.add(builder);
            }
            return this.sequence(list);
        }
        public StateMachineBuilder accept(String accept) {
            this.start.val = new AcceptingState(accept);
            return this;
        }
        protected StateMachineBuilder empty() {
            this.start.val = new EmptyState();
            return this;
        }
        protected StateMachineBuilder _null() {
            this.start.val = new NullState();
            return this;
        }
        protected StateMachineBuilder optional(StateMachineBuilder optional) {
            return this.branch(optional, new StateMachineBuilder().empty());
        }
        protected StateMachineBuilder _list0(StateMachineBuilder list) {
            return this.branch(new StateMachineBuilder().sequence(list, this), new StateMachineBuilder().empty());
        }
        public StateMachineBuilder list0(StateMachineBuilder list, String accept) {
            return this._list(new StateMachineBuilder()._list0(list), accept);
        }
        public StateMachineBuilder list1(StateMachineBuilder list, String accept) {
            return this._list(new StateMachineBuilder().sequence(list, new StateMachineBuilder()._list0(list)), accept);
        }
        public StateMachineBuilder _list(StateMachineBuilder list, String accept) {
            return this.sequence(new StateMachineBuilder()._null(), list, new StateMachineBuilder().accept(accept));
        }
        public StateMachineBuilder nullable(StateMachineBuilder nullable, String accept) {
            StateMachineBuilder _null_ = new StateMachineBuilder()._null();
            return this.sequence(new StateMachineBuilder().branch(new StateMachineBuilder().sequence(_null_, nullable), _null_), new StateMachineBuilder().accept(accept));
        }
        public StateMachineBuilder read(HashMap<Character, StateMachineBuilder> map) {
            HashMap<Character, Pointer<StateMachine>> m = new HashMap<Character, Pointer<StateMachine>>();
            for(Entry<Character, StateMachineBuilder> entry : map.entrySet()) {
                m.put(entry.getKey(), entry.getValue().start);
            }
            this.start.val = new ReadCharacterState(m);
            return this;
        }
    }
    protected static class Pointer<T extends Serializable> implements Serializable {
        public T val;
    }
    protected static abstract class StateMachine implements Serializable {
        protected abstract void enter(ParseState state);
    }
    protected static class BranchState extends StateMachine {
        private final Pointer<StateMachine> next, fail;
        protected BranchState(Pointer<StateMachine> next, Pointer<StateMachine> fail) {
            this.next = next; this.fail = fail;
        }
        protected void enter(ParseState state) {
            state.branch(next.val, fail.val);
        }
    }
    protected static class SequenceState extends StateMachine {
        private final ArrayList<Pointer<StateMachine>> seq;
        protected SequenceState(ArrayList<Pointer<StateMachine>> sequence) {
            this.seq = sequence;
        }
        protected void enter(ParseState state) {
            for(Pointer<StateMachine> ptr : this.seq) {
                state.push(ptr.val);
            }
        }
    }
    protected static class AcceptingState extends StateMachine {
        private final String accept;
        protected AcceptingState(String accept) {
            this.accept = accept;
        }
        protected void enter(ParseState state) {
            state.accept(accept);
        }
    }
    protected static class NullState extends AcceptingState {
        protected NullState() {
            super("null");
        }
    }
    protected static class ReadCharacterState extends StateMachine {
        private final HashMap<Character, Pointer<StateMachine>> map;
        protected ReadCharacterState(HashMap<Character, Pointer<StateMachine>> map) {
            this.map = map;
        }
        protected void enter(ParseState state) {
            if(!state.stream.hasNext()) {
                state.fail();
                return;
            }
            char c = state.stream.next();
            Pointer<StateMachine> ptr = map.get(c);
            if(ptr == null) {
                state.fail();
                return;
            }
            state.push(ptr.val);
            state.push(c);
        }
    }
    protected static class EmptyState extends StateMachine {
        protected void enter(ParseState state) { }
    }

    private final StateMachine statemachine;
    public Parser(StateMachine statemachine) {
        this.statemachine = statemachine;
    }

    private static class ParseState {
        private final Stack<Stack<StateMachine>> states = new Stack<Stack<StateMachine>>();
        private final Stack<StateMachine> fail = new Stack<StateMachine>();
        public final Stack<Object> parseStack = new Stack<Object>();
        public final Stack<StringBuffer> tokens = new Stack<StringBuffer>();
        public final StringStream stream;
        private StringBuffer token = new StringBuffer();
        public ParseState(String toParse, StateMachine start) {
            this.stream = new StringStream(toParse);
            states.push(new Stack<StateMachine>());
            this.push(start);
        }
        public void push(StateMachine state) {
            this.state().push(state);
        }
        public void push(char c) {
            this.token.append(c);
        }
        public Stack<StateMachine> state() {
            return this.states.peek();
        }
        public void branch(StateMachine next, StateMachine fail) {
            if(next == null || fail == null) {
                throw new IllegalArgumentException();
            }
            stream.save();
            states.push(new Stack<StateMachine>());
            this.state().push(next);
            this.tokens.push(new StringBuffer().append(this.token.toString()));
        }
        public void fail() {
            this.states.pop();
            this.state().push(this.fail.pop());
            this.token = this.tokens.pop();
        }
        public boolean hasNext() {
            if(this.state().size() > 0) {
                return true;
            }
            if(this.states.size() > 1) {
                this.states.pop();
                this.fail.pop();
                this.tokens.pop();
                return this.hasNext();
            }
            return false;
        }
        public StateMachine next() {
            if(this.hasNext()) {
                return this.state().pop();
            }
            throw new IllegalArgumentException();
        }
        public void accept(String accept) {
            if(accept != null) {
                if(accept.equals("String")) {
                    parseStack.push(token.toString());
                    token = new StringBuffer();
                }else if(accept.equals("null")) {
                    parseStack.push(null);
                }else {
                    if(accept.endsWith(Parser.listSuffix())) {
                        ArrayList<Object> list = new ArrayList<Object>();
                        for(Object o = parseStack.pop(); o != null; o = parseStack.pop()) {
                            list.add(o);
                        }
                        parseStack.push(list);
                    }else if(accept.endsWith(Parser.nullableSuffix())) {
                        accept(accept.substring(0, accept.indexOf(Parser.nullableSuffix())));
                    }
                    try {
                        Object o = Class.forName(accept).getConstructor().newInstance();
                        Field[] f = Interpreter.getSortedFields(o);
                        for(int i = f.length - 1; i >= 0; i--) {
                            f[i].set(o, parseStack.pop());
                        }
                        parseStack.push(o);
                    }catch(Exception e) {
                        e.printStackTrace();
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
    }

    public Object parse(String text) {

        if(this.statemachine == null) {
            return null;
        }
        ParseState state = new ParseState(text, this.statemachine);
        while(state.stream.hasNext() && state.hasNext()) {
            state.next().enter(state);
        }
        if(state.states.size() == 0 && state.parseStack.size() == 1) {
            return state.parseStack.pop();
        }
        return null;
    }
}
