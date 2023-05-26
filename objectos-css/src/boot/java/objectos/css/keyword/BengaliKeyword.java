package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class BengaliKeyword extends StandardKeyword implements CounterStyleValue {
  static final BengaliKeyword INSTANCE = new BengaliKeyword();

  private BengaliKeyword() {
    super(24, "bengali", "bengali");
  }
}
