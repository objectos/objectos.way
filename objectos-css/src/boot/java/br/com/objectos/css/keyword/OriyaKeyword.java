package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class OriyaKeyword extends StandardKeyword implements CounterStyleValue {
  static final OriyaKeyword INSTANCE = new OriyaKeyword();

  private OriyaKeyword() {
    super(175, "oriya", "oriya");
  }
}
