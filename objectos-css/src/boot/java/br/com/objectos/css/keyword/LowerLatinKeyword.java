package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class LowerLatinKeyword extends StandardKeyword implements CounterStyleValue {
  static final LowerLatinKeyword INSTANCE = new LowerLatinKeyword();

  private LowerLatinKeyword() {
    super(141, "lowerLatin", "lower-latin");
  }
}
