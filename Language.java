
public class Language {

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter(new Language());
        $program p = new $expression_list();
        interpreter.interpret(p);
    }

    public void handle($expression_list exp) {
        System.out.println(exp);
    }
}
