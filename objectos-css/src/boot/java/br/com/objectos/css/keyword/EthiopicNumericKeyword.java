package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class EthiopicNumericKeyword extends StandardKeyword implements CounterStyleValue {
  static final EthiopicNumericKeyword INSTANCE = new EthiopicNumericKeyword();

  private EthiopicNumericKeyword() {
    super(74, "ethiopicNumeric", "ethiopic-numeric");
  }
}
