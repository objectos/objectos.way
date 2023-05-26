package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class LowerRomanKeyword extends StandardKeyword implements CounterStyleValue {
  static final LowerRomanKeyword INSTANCE = new LowerRomanKeyword();

  private LowerRomanKeyword() {
    super(142, "lowerRoman", "lower-roman");
  }
}
