package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class HiraganaKeyword extends StandardKeyword implements CounterStyleValue {
  static final HiraganaKeyword INSTANCE = new HiraganaKeyword();

  private HiraganaKeyword() {
    super(100, "hiragana", "hiragana");
  }
}
