package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class PersianKeyword extends StandardKeyword implements CounterStyleValue {
  static final PersianKeyword INSTANCE = new PersianKeyword();

  private PersianKeyword() {
    super(180, "persian", "persian");
  }
}
