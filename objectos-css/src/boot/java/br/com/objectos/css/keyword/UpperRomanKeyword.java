package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class UpperRomanKeyword extends StandardKeyword implements CounterStyleValue {
  static final UpperRomanKeyword INSTANCE = new UpperRomanKeyword();

  private UpperRomanKeyword() {
    super(268, "upperRoman", "upper-roman");
  }
}
