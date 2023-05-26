package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class CjkDecimalKeyword extends StandardKeyword implements CounterStyleValue {
  static final CjkDecimalKeyword INSTANCE = new CjkDecimalKeyword();

  private CjkDecimalKeyword() {
    super(42, "cjkDecimal", "cjk-decimal");
  }
}
