package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class UpperLatinKeyword extends StandardKeyword implements CounterStyleValue {
  static final UpperLatinKeyword INSTANCE = new UpperLatinKeyword();

  private UpperLatinKeyword() {
    super(267, "upperLatin", "upper-latin");
  }
}
