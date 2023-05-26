package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class BengaliKeyword extends StandardKeyword implements CounterStyleValue {
  static final BengaliKeyword INSTANCE = new BengaliKeyword();

  private BengaliKeyword() {
    super(24, "bengali", "bengali");
  }
}
