import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class Interpreter<T> {

    private final Object interpreter;
    private final HashMap<Class, Method> methods;
    public Interpreter(Object interpreter) {
        this.interpreter = interpreter;
        methods = new HashMap<Class, Method>();
        for(Method m : interpreter.getClass().getMethods()) {
            String name = m.getName();
            Class<?> params[] = m.getParameterTypes();
            if(name.equals("handle") && params.length == 1) {
                methods.put(params[0], m);
            }
        }
    }

    private static final Comparator<Field> sort = (Field f1, Field f2) -> {
        return f1.getName().compareTo(f2.getName());
    };

    public void interpret(T program) {
        try {
            _interpret_(new Object[]{ program });
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    static class UnhandledSyntaxException extends Exception {
        public UnhandledSyntaxException() { }
        public UnhandledSyntaxException(String msg) {
            super(msg);
        }
    }
    private boolean _interpret_(Object[] program) throws Exception {
        for(Object o : program) {
            Method m = methods.get(o.getClass());
            if(m != null) {
                m.invoke(interpreter, o);
                return true;
            }
            Field[] fields = o.getClass().getFields();
            System.out.println(Arrays.toString(fields));
            if(fields.length != 0) {
                Arrays.sort(fields, sort);
                Object[] ofields = new Object[fields.length];
                for(int i = 0; i < fields.length; i++) {
                    ofields[i] = fields[i].get(o);
                }
                return _interpret_(ofields);
            }
            throw new UnhandledSyntaxException(String.format("Unhandled class: %s", o.getClass().getName()));
        }
        return false;
    }
}
