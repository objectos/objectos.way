package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class DecimalKeyword extends StandardKeyword implements CounterStyleValue {
  static final DecimalKeyword INSTANCE = new DecimalKeyword();

  private DecimalKeyword() {
    super(62, "decimal", "decimal");
  }
}
