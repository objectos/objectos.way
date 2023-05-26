package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class DecimalLeadingZeroKeyword extends StandardKeyword implements CounterStyleValue {
  static final DecimalLeadingZeroKeyword INSTANCE = new DecimalLeadingZeroKeyword();

  private DecimalLeadingZeroKeyword() {
    super(63, "decimalLeadingZero", "decimal-leading-zero");
  }
}
