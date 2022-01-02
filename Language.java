
public class Language {

    public static void main(String[] args) {
        Interpreter<$program> interpreter = new Interpreter<$program>(new Language());
        interpreter.interpret(new $expression_list());
    }

    public void handle($expression_list exp) {
        System.out.println(exp);
    }
}
