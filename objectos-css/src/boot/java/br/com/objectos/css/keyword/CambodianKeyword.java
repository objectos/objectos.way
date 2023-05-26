package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class CambodianKeyword extends StandardKeyword implements CounterStyleValue {
  static final CambodianKeyword INSTANCE = new CambodianKeyword();

  private CambodianKeyword() {
    super(35, "cambodian", "cambodian");
  }
}
