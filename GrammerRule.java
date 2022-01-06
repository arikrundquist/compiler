
public abstract class GrammerRule {

    public static RegexRule regex(String regex) {
        return new RegexRule(regex);
    }
    public static BuildRule build(String name) {
        return new BuildRule(name);
    }
    public static AcceptRule accept(String name) {
        return new AcceptRule(name);
    }
    public static GrammerRule _null() {
        return GrammerRule.accept("null");
    }

    public static class RegexRule extends GrammerRule {
        private final String regex;
        protected RegexRule(String regex) { this.regex = regex; }
    }
    public static class BuildRule extends GrammerRule {
        private final String name;
        protected BuildRule(String name) { this.name = name; }
    }
    public static class AcceptRule extends GrammerRule {
        private final String name;
        protected AcceptRule(String name) { this.name = name; }
    }
}
