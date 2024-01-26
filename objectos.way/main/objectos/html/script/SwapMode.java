package objectos.html.script;

public final class SwapMode {

  public static final SwapMode INNER_HTML = new SwapMode("innerHTML");

  public static final SwapMode OUTER_HTML = new SwapMode("outerHTML");

  private final String value;

  private SwapMode(String value) {
    this.value = value;
  }

  public final String value() {
    return value;
  }

}
