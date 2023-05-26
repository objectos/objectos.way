package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class ThaiKeyword extends StandardKeyword implements CounterStyleValue {
  static final ThaiKeyword INSTANCE = new ThaiKeyword();

  private ThaiKeyword() {
    super(255, "thai", "thai");
  }
}
