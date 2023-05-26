package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class DecimalLeadingZeroKeyword extends StandardKeyword implements CounterStyleValue {
  static final DecimalLeadingZeroKeyword INSTANCE = new DecimalLeadingZeroKeyword();

  private DecimalLeadingZeroKeyword() {
    super(63, "decimalLeadingZero", "decimal-leading-zero");
  }
}
