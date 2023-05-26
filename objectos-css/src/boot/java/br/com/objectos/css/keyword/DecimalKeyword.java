package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class DecimalKeyword extends StandardKeyword implements CounterStyleValue {
  static final DecimalKeyword INSTANCE = new DecimalKeyword();

  private DecimalKeyword() {
    super(62, "decimal", "decimal");
  }
}
