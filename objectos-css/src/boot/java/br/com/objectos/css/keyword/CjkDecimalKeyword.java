package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class CjkDecimalKeyword extends StandardKeyword implements CounterStyleValue {
  static final CjkDecimalKeyword INSTANCE = new CjkDecimalKeyword();

  private CjkDecimalKeyword() {
    super(42, "cjkDecimal", "cjk-decimal");
  }
}
