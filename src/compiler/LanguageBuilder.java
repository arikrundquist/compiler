
package compiler;

import util.Pointer;

public final class LanguageBuilder {

    private final StringBuffer lists = new StringBuffer(), tokens = new StringBuffer(), options = new StringBuffer(), rules = new StringBuffer(), nullables = new StringBuffer();
    private final java.util.HashMap<Pointer<? extends LangElementBuilder>, String> qualifiedNames = new java.util.HashMap<Pointer<? extends LangElementBuilder>, String>();
    private final java.util.HashMap<String, Pointer<? extends LangElementBuilder>> pointers = new java.util.HashMap<String, Pointer<? extends LangElementBuilder>>();

    private static final String LISTS = "Lists", OPTIONS = "Optionals", TOKENS = "Tokens", RULES = "Rules", NULLABLES = "Nullables";

    public boolean build(String dir) {
        try(java.io.FileWriter writer = new java.io.FileWriter(dir + "/" + _package.replace('.', '/') + "/" + name + ".java")) {
            writer.write(genClassFile(_package, name));
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private static String subclass(String name, StringBuffer contents) {
        return
            "\tpublic static final class " + name + " {\n" +
            "\t\tprivate " + name + "() { }\n\n" +
            contents.toString() +
            "\t}\n";
    }
    private String genClassFile(String _package, String name) {
        StringBuffer sb = new StringBuffer();
        sb.append(
            "\npackage " + _package + ";\n\n" +
            "public final class " + name + " extends compiler.Language {\n" +
            "\tprivate " + name + "() {\n"
        );
        for(String qualifiedName : this.qualifiedNames.values()) {
            sb.append("\t\tthis.add(" + qualifiedName + ".class);\n");
        }
        sb.append("\t}\n\n");
        
        sb.append(subclass(TOKENS, this.tokens));
        sb.append(subclass(RULES, this.rules));
        sb.append(subclass(LISTS, this.lists));
        sb.append(subclass(OPTIONS, this.options));
        sb.append(subclass(NULLABLES, this.nullables));
        
        return sb.append("}\n").toString();
    }

    private final String name, _package;
    public LanguageBuilder(String name, String _package) {
        this.name = name; this._package = _package;
    }

    private void register(Pointer<? extends LangElementBuilder> ptr, String qualifiedName) {
        this.qualifiedNames.put(ptr, qualifiedName);
    }

    public Pointer<SyntaxRuleBuilder> token(String name, String regex, Pointer<OptionRuleBuilder> _super) {
        Pointer<TokenBuilder> token = new Pointer<TokenBuilder>(new TokenBuilder(name, regex));
        this.register(token, TOKENS + "." + name);
        this.tokens.append(token.value.getClassString(this));
        Pointer<SyntaxRuleBuilder> wrapper = this.rule(name, _super);
        this.define(wrapper, token);
        return wrapper;
    }
    public Pointer<SyntaxRuleBuilder> token(String name, String regex) {
        return this.token(name, regex, null);
    }

    public Pointer<OptionRuleBuilder> option(String name, Pointer<OptionRuleBuilder> _super) {
        Pointer<OptionRuleBuilder> option = new Pointer<OptionRuleBuilder>(new OptionRuleBuilder(name, _super));
        this.register(option, OPTIONS + "." + name);
        
        return option;
    }
    public Pointer<OptionRuleBuilder> option(String name) { return this.option(name, null); }
    public Pointer<SyntaxRuleBuilder> rule(String name, Pointer<OptionRuleBuilder> _super) {
        Pointer<SyntaxRuleBuilder> rule = new Pointer<SyntaxRuleBuilder>(new SyntaxRuleBuilder(name, _super));
        this.register(rule, RULES + "." + name);
        return rule;
    }
    public Pointer<SyntaxRuleBuilder> rule(String name) { return this.rule(name, null); }
    public enum ListMode {ZEROFEW, ZEROMANY, ONEFEW, ONEMANY};
    public Pointer<ListRuleBuilder> list(String name, Pointer<? extends LangElementBuilder> toList, ListMode mode) {
        Pointer<ListRuleBuilder> list = new Pointer<ListRuleBuilder>(new ListRuleBuilder(name, toList, mode));
        this.lists.append(list.value.getClassString(this));
        return list;
    }
    public Pointer<NullableRuleBuilder> nullable(String name, Pointer<LangElementBuilder> toMakeNullable) {
        Pointer<NullableRuleBuilder> nullable = new Pointer<NullableRuleBuilder>(new NullableRuleBuilder(name, toMakeNullable));
        this.register(nullable, NULLABLES + "." + name);
        return nullable;
    }
    



    public static abstract class LangElementBuilder {
        protected final String name;
        private LangElementBuilder(String name) {
            if(!name.matches("^[_a-zA-Z$][_a-zA-Z$0-9]*$")) {
                throw new IllegalArgumentException(name + " is an invalid name");
            }
            this.name = name;
        }
        protected String getClassString(LanguageBuilder builder) {
            return this.getClassString("", null);
        }
        protected final String getClassString(String contents, String _extends) {
            return
                "\t\tpublic static class " + this.name + (_extends == null ? "" : " extends " + _extends) + " {\n" +
                contents +
                "\t\t}\n";
        }
        protected final String getAbstractClassString(String contents, String _extends) {
            return
                "\t\tpublic static abstract class " + this.name + (_extends == null ? "" : " extends " + _extends) + " {\n" +
                contents +
                "\t\t}\n";
        }
    }
    private static abstract class PrimativeRuleBuilder extends LangElementBuilder {
        private PrimativeRuleBuilder(String name) {
            super(name);
        }
    }
    private static final class TokenBuilder extends PrimativeRuleBuilder {
        private final String regex;
        private TokenBuilder(String name, String regex) {
            super(name);
            this.regex = regex;
        }
        protected String getClassString(LanguageBuilder builder) {
            return super.getClassString("\t\t\tpublic " + this.name + "() { super(\"" + TokenBuilder.escape(this.regex) + "\"); }\n", "compiler.syntax.Token");
        }
        private static String escape(String string) {
            StringBuffer sb = new StringBuffer();
            for(char c : string.toCharArray()) {
                switch(c) {
                case '\n':
                case '\r':
                case '\t':
                case '\"':
                case '\'':
                case '\f':
                case '\b':
                case '\\':
                    sb.append('\\');
                default:
                    sb.append(c);
                }
            }
            return sb.toString();
        }
    }
    public static final class ListRuleBuilder extends PrimativeRuleBuilder {
        private final ListMode mode;
        private final Pointer<? extends LangElementBuilder> toList;
        private ListRuleBuilder(String name, Pointer<? extends LangElementBuilder> toList, ListMode mode) {
            super(name);
            this.mode = mode;
            this.toList = toList;
        }
        protected String getClassString(LanguageBuilder builder) {
            String type = null;
            switch(this.mode) {
                case ZEROFEW: type = "compiler.syntax.ZeroFewList"; break;
                case ZEROMANY: type = "compiler.syntax.ZeroManyList"; break;
                case ONEFEW: type = "compiler.syntax.OneFewList"; break;
                case ONEMANY: type = "compiler.syntax.OneManyList"; break;
            }
            type += String.format("<%s>", builder.qualifiedNames.get(this.toList));
            return super.getClassString("\t\t\tpublic " + this.name + "() { super(" + builder.qualifiedNames.get(this.toList) + ".class); }\n", type);
        }
    }
    public static final class NullableRuleBuilder extends PrimativeRuleBuilder {
        private final Pointer<LangElementBuilder> toMakeNullable;
        private NullableRuleBuilder(String name, Pointer<LangElementBuilder> toMakeNullable) {
            super(name);
            this.toMakeNullable = toMakeNullable;
        }
        protected String getClassString(LanguageBuilder builder) {
            return super.getClassString("\t\t\tpublic " + this.name + "() { super(" + builder.qualifiedNames.get(this.toMakeNullable) + ".class); }\n", String.format("compiler.syntax.Nullable<%s>", builder.qualifiedNames.get(this.toMakeNullable)));
        }
    }
    private static abstract class RuleBuilder extends LangElementBuilder {
        protected Pointer<OptionRuleBuilder> _super;
        private RuleBuilder(String name, Pointer<OptionRuleBuilder> _super) {
            super(name);
            this._super = _super;
        }
    }
    public static final class OptionRuleBuilder extends RuleBuilder {
        private OptionRuleBuilder(String name, Pointer<OptionRuleBuilder> _super) {
            super(name, _super);
        }
        protected String getClassString(LanguageBuilder builder) {
            return super.getAbstractClassString("", builder.qualifiedNames.get(this._super));
        }
    }
    public static final class SyntaxRuleBuilder extends RuleBuilder {
        private java.util.List<Pointer<? extends LangElementBuilder>> rule = new java.util.ArrayList<Pointer<? extends LangElementBuilder>>();
        private SyntaxRuleBuilder(String name, Pointer<OptionRuleBuilder> _super) {
            super(name, _super);
        }
        protected String getClassString(LanguageBuilder builder) {
            return null;
        }
    }
    @SafeVarargs
    public final void define(Pointer<SyntaxRuleBuilder> rule, Pointer<? extends LangElementBuilder>... rules) {
        if(rule.value.rule.size() > 0) {
            throw new IllegalArgumentException("Rule already defined");
        }
        for(Pointer<? extends LangElementBuilder> p : rules) {
            rule.value.rule.add(p);
        }
    }
}
