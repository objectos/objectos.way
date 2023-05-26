package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class UpperRomanKeyword extends StandardKeyword implements CounterStyleValue {
  static final UpperRomanKeyword INSTANCE = new UpperRomanKeyword();

  private UpperRomanKeyword() {
    super(268, "upperRoman", "upper-roman");
  }
}
