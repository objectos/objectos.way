package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class LowerLatinKeyword extends StandardKeyword implements CounterStyleValue {
  static final LowerLatinKeyword INSTANCE = new LowerLatinKeyword();

  private LowerLatinKeyword() {
    super(141, "lowerLatin", "lower-latin");
  }
}
