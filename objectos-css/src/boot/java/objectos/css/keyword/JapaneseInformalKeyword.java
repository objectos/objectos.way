package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class JapaneseInformalKeyword extends StandardKeyword implements CounterStyleValue {
  static final JapaneseInformalKeyword INSTANCE = new JapaneseInformalKeyword();

  private JapaneseInformalKeyword() {
    super(118, "japaneseInformal", "japanese-informal");
  }
}
