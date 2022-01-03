
import java.util.Stack;

public class StringStream {
    private final String stream;
    private int index = 0;
    private final Stack<Integer> saves = new Stack<Integer>();
    public StringStream(String toStream) {
        this.stream = toStream;
    }
    public boolean hasNext() {
        return this.index < this.stream.length();
    }
    public char next() {
        return this.stream.charAt(index++);
    }
    public char peek() {
        return this.stream.charAt(index);
    }
    public void save() {
        this.saves.push(index);
    }
    public void pass() {
        this.saves.pop();
    }
    public void fail() {
        this.index = this.saves.pop();
    }
}
