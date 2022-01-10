
import compiler.LanguageBuilder;
import compiler.LanguageBuilder.*;
import util.Pointer;

public class Runner {

    public static void main() {
        LanguageBuilder builder = new LanguageBuilder("Lang", "lang");

        // declare options
        //Pointer<OptionRuleBuilder>

        // declare syntax
        //Pointer<SyntaxRuleBuilder>

        // define tokens
        Pointer<? extends LangElementBuilder>
            int_t = builder.token("int_t", "[0-9]+");

        // define nullables and lists

        // define syntax

    }
}
