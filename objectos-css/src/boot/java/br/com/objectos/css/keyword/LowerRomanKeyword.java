package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class LowerRomanKeyword extends StandardKeyword implements CounterStyleValue {
  static final LowerRomanKeyword INSTANCE = new LowerRomanKeyword();

  private LowerRomanKeyword() {
    super(142, "lowerRoman", "lower-roman");
  }
}
