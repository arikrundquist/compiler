
package lang;

public final class Lang extends compiler.Language {
	private Lang() {
		this.add(Tokens.A.class);
	}

	public static final class Tokens {
		private Tokens() { }

		public static class A extends compiler.syntax.Token {
			public A() { super("[aA]"); }
		}
	}
	public static final class Rules {
		private Rules() { }

	}
	public static final class Lists {
		private Lists() { }

		public static class AList extends compiler.syntax.OneManyList<Tokens.A> {
			public AList() { super(Tokens.A.class); }
		}
	}
	public static final class Optionals {
		private Optionals() { }

	}
	public static final class Nullables {
		private Nullables() { }

	}
}
