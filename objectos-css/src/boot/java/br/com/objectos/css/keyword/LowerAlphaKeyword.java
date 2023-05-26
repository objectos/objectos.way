package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class LowerAlphaKeyword extends StandardKeyword implements CounterStyleValue {
  static final LowerAlphaKeyword INSTANCE = new LowerAlphaKeyword();

  private LowerAlphaKeyword() {
    super(138, "lowerAlpha", "lower-alpha");
  }
}
