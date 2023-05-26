package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class UpperAlphaKeyword extends StandardKeyword implements CounterStyleValue {
  static final UpperAlphaKeyword INSTANCE = new UpperAlphaKeyword();

  private UpperAlphaKeyword() {
    super(265, "upperAlpha", "upper-alpha");
  }
}
