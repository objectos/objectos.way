package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class ArmenianKeyword extends StandardKeyword implements CounterStyleValue {
  static final ArmenianKeyword INSTANCE = new ArmenianKeyword();

  private ArmenianKeyword() {
    super(21, "armenian", "armenian");
  }
}
