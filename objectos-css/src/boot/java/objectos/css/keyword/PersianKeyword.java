package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class PersianKeyword extends StandardKeyword implements CounterStyleValue {
  static final PersianKeyword INSTANCE = new PersianKeyword();

  private PersianKeyword() {
    super(180, "persian", "persian");
  }
}
