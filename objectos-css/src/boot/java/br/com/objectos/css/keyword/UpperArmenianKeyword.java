package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class UpperArmenianKeyword extends StandardKeyword implements CounterStyleValue {
  static final UpperArmenianKeyword INSTANCE = new UpperArmenianKeyword();

  private UpperArmenianKeyword() {
    super(266, "upperArmenian", "upper-armenian");
  }
}
