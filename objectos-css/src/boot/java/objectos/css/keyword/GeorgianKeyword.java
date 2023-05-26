package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class GeorgianKeyword extends StandardKeyword implements CounterStyleValue {
  static final GeorgianKeyword INSTANCE = new GeorgianKeyword();

  private GeorgianKeyword() {
    super(88, "georgian", "georgian");
  }
}
