
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SynReader {

  private static abstract class Token { }
  private static class Regex extends Token {
    public final String regex;
    public Regex(String regex) {
      this.regex = regex;
    }
    public String toString() {
      return "<regex>" + this.regex + "</regex>";
    }
  }
  private static class Operator extends Token {
    public final String operator;
    public Operator(String operator) {
      this.operator = operator;
    }
    public String toString() {
      return "<op>" + this.operator + "</op>";
    }
  }
  private static class Name extends Token {
    public final String name;
    private Name(String name) {
      names.put(name, this);
      this.name = "$" + name;
    }
    private static final HashMap<String, Name> names = new HashMap<String, Name>();
    public static Name newName(String name) {
      Name n = names.get(name);
      if(n == null) {
        n = new Name(name);
      }
      return n;
    }
    public String toString() {
      return "<name>" + this.name + "</name>";
    }
  }
  private static class List extends Token {
    public Token token; public Operator operator;
    public List(Token token, Operator operator) {
      this.token = token;
      this.operator = operator;
    }
    public String toString() {
      return "<list>" + this.token + this.operator + "</list>";
    }
  }
  private static class Target {
    public Name name;
    public ArrayList<Token> tokens = new ArrayList<Token>();
    public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("<target>\n" + this.name + "\n<list>");
      for(Token t : this.tokens) {
        sb.append(t);
      }
      sb.append("</list>\n</target>");
      return sb.toString();
    }
  }
  private static class SynObject {
    public Name name;
    public ArrayList<Target> targets = new ArrayList<Target>();
    public SynObject(Name name) {
      this.name = name;
    }
    public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("<synobject>\n" + this.name + "\n");
      for(Target t : this.targets) {
        sb.append(t);
        sb.append("\n");
      }
      sb.append("</synobject>");
      return sb.toString();
    }
  }

  private static class StringStream {
    private final String str;
    private int index = 0;
    private final int length;
    private Stack<Integer> saves = new Stack<Integer>();
    public StringStream(String s) {
      str = s;
      length = str.length();
    }
    public char peek() {
      return str.charAt(index);
    }
    public char next() {
      return str.charAt(index++);
    }
    public boolean hasNext() {
      return index < length;
    }
    public void save() {
      saves.push(index);
    }
    public void fail() {
      index = saves.pop();
    }
    public void pass() {
      saves.pop();
    }
    private boolean isWhite(char c) {
      return c == ' ' || c == '\n' || c == '\r';
    }
    public void skipWhite() {
      while(this.hasNext() && isWhite(this.peek())) {
        this.next();
      }
    }
  }

  private ArrayList<SynObject> objects = new ArrayList<SynObject>();

  private static boolean acceptIdentifier(char c) {
    if('A' <= c && c <= 'Z') { return true; }
    if('a' <= c && c <= 'z') { return true; }
    if(c == '_') { return true; }
    return false;
  }
  private static Name getName(StringStream stream) {
    String name = "";
    while(stream.hasNext() && acceptIdentifier(stream.peek())) {
      name += stream.next();
    }
    if(name.equals("")) { return null; }
    return Name.newName(name);
  }
  private static Operator getOperator(StringStream stream) {
    /*char c = stream.peek();
    if(c == '?') {
      return new Operator("" + stream.next());
    }
    if(c == '*' || c == '+') {
      String operator = "" + stream.next();
      // removing support for *? and +? operators
      /*if(stream.peek() == '?') {
        operator += stream.next();
      }* /
      return new Operator(operator);
    }*/
    return null;
  }
  private static Regex getRegex(StringStream stream) {
    char c1 = stream.peek();
    if(c1 != '"' && c1 != '[') { return null; }
    stream.next();
    String regex = (c1 == '"' ? "" : "[");
    while(true) {
      char c2 = stream.next();
      if(c1 == '"' && c2 == '"') { return new Regex(regex); }
      regex += c2;
      if(c1 == '[' && c2 == ']') { return new Regex(regex); }
      if(c2 == '\\') { regex += stream.next(); } // skip a character
    }
  }
  private static Name getSynObjectName(StringStream stream) {
    while(stream.hasNext() && stream.next() != '@');
    if(!stream.hasNext()) { return null; }
    return getName(stream);
  }
  private static Token getToken(StringStream stream) {
    if(!stream.hasNext()) {
      return null;
    }
    Token t;
    t = getName(stream);
    if(t != null) { return t; }
    t = getRegex(stream);
    if(t != null) { return t; }
    t = getOperator(stream);
    if(t != null) { return t; }
    return null;
  }
  private static Target getTarget(StringStream stream) {
    stream.save();
    stream.skipWhite();
    Name name = getName(stream);
    if(name == null) { return null; }
    Target t = new Target();
    t.name = name;
    stream.skipWhite();
    if(stream.next() != ':') {
      stream.fail();
      return null;
    }
    stream.skipWhite();
    while(true) {
      stream.save();
      Token tok = getToken(stream);
      stream.skipWhite();
      if(tok == null || (stream.hasNext() && stream.peek() == ':')) {
        stream.fail();
        break;
      }
      t.tokens.add(tok);
      stream.pass();
    }
    return handleOperators(t);
  }
  private static Target handleOperators(Target t) {
    if(t == null) {
      return t;
    }
    for(int i = 0; i < t.tokens.size() - 1; i++) {
      Token tok = t.tokens.get(i + 1);
      if(tok instanceof Operator) {
        t.tokens.set(i, new List(t.tokens.remove(i), (Operator) tok));
        i--;
      }
    }
    return t;
  }
  private static SynObject getSynObject(StringStream stream) {
    Name name = getSynObjectName(stream);
    if(name == null) { return null; }
    SynObject syn = new SynObject(name);
    while(true) {
      Target t = getTarget(stream);
      if(t == null) {
        break;
      }
      syn.targets.add(t);
    }
    return syn;
  }

  private void interpret(String contents) {
    StringStream stream = new StringStream(contents);
    while(true) {
      SynObject so = getSynObject(stream);
      if(so == null) {
        break;
      }
      objects.add(so);
    }
    /*for(SynObject syn : objects) {
      System.out.println(syn);
    }*/
  }

  public SynReader(String filename) {
    try {
      File f = new File(filename);
      Scanner s = new Scanner(f);
      StringBuffer sb = new StringBuffer();
      while(s.hasNextLine()) {
        sb.append(s.nextLine());
        sb.append("\n");
      }
      interpret(sb.toString());
      this.writeProgramClass();
      //System.out.println(this.objects);
      s.close();
    }catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    if(args.length > 0) {
      new SynReader(args[0]);
    }
  }

  private void writeProgramClass() {
    StringBuffer sb = new StringBuffer();
    HashSet<String> neededLists = new HashSet<String>();
    HashSet<String> neededNullables = new HashSet<String>();
    sb.append("import java.util.ArrayList;\n\n");
    boolean first = true;
    FileWriter fw = null;
    for(SynObject so : this.objects) {
      sb.append(String.format("%sabstract class %s {", first ? "public " : "", so.name.name));
      if(first) {
        sb.append(ParserWriter.writeParserMethod(this.objects) + "\n");
        try {
          fw = new FileWriter(so.name.name + ".java");
        }catch(Exception e) {
          e.printStackTrace();
          return;
        }
      }
      sb.append(first ? "}\n" : " }\n");
      first = false;
      for(Target t : so.targets) {
        sb.append(String.format("class %s extends %s {\n", t.name.name, so.name.name));
        int id = 0;
        for(Token tok : t.tokens) {
          id++;
          String name;
          if(tok instanceof List) {
            name = handleList((List) tok, neededLists, neededNullables);
          }else {
            if(tok instanceof Name) {
              name = ((Name) tok).name;
            }else if(tok instanceof Regex){
              name = "String";
            }else { continue; }
          }
          sb.append(String.format("\tpublic %s f%d;\n", name, id));
        }
        sb.append("}\n");
      }
    }
    for(String list : neededLists) {
      sb.append(String.format("class %s {\n\tpublic ArrayList<%s> list = new ArrayList<%s>();\n}\n", getListName(list), list, list));
    }
    for(String nullable : neededNullables) {
      sb.append(String.format("class %s {\n\tpublic %s nullable;\n}\n", getNullableName(nullable), nullable));
    }
    if(fw != null) {
      try {
        fw.write(sb.toString());
        fw.close();
      }catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  private String getNullableName(String type) {
    return type + "$nullable";
  }
  private String getListName(String type) {
    return type + "$list";
  }
  private String handleList(List l, HashSet<String> neededLists, HashSet<String> neededNullables) {
    String type;
    if(l.token instanceof Name) {
      type = ((Name) l.token).name;
    }else if(l.token instanceof Regex) {
      type = "String";
    }else if(l.token instanceof List) {
      type = handleList((List) l.token, neededLists, neededNullables);
    }else {
      throw new IllegalArgumentException("Did not expect type " + l.token.getClass().getSimpleName());
    }
    Operator o = l.operator;
    if(o.operator.equals("?")) {
      neededNullables.add(type);
      return getNullableName(type);
    }else {
      neededLists.add(type);
      return(getListName(type));
    }
  }

  static class ParserWriter {

    public static String writeParserMethod(ArrayList<SynObject> objects) {
      StringBuffer sb = new StringBuffer();
      
      Parser p = null;// = new Parser();
      String name = objects.get(0).name.name;
      try(
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)
      ) {  
        oos.writeObject(p);
        sb.append(
          "\tprivate static Parser parser;\n" +
          "\tprivate static final byte[] ba = " + Arrays.toString(baos.toByteArray()).replace('[', '{').replace(']', '}') + ";\n" +
          "\tstatic {\n" +
          "\t\ttry(\n" +
          "\t\t\tjava.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(ba);\n" +
          "\t\t\tjava.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais)\n" +
          "\t\t) {\n" +
          "\t\t\tparser = (Parser) ois.readObject();\n" +
          "\t\t}catch(Exception e) {\n" +
          "\t\t\te.printStackTrace();\n" +
          "\t\t}\n" +
          "\t}\n\n" +
          "\tpublic static final " + name + " parse(String code) {\n" +
          "\t\treturn (" + name + ") parser.parse(code);\n" +
          "\t}\n"
        );
      }catch(Exception e) {
        e.printStackTrace();
      }
      return sb.toString();
    }
  }
}
