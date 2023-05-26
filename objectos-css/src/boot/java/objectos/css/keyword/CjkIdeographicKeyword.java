package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class CjkIdeographicKeyword extends StandardKeyword implements CounterStyleValue {
  static final CjkIdeographicKeyword INSTANCE = new CjkIdeographicKeyword();

  private CjkIdeographicKeyword() {
    super(45, "cjkIdeographic", "cjk-ideographic");
  }
}
