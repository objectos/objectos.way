package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class KatakanaKeyword extends StandardKeyword implements CounterStyleValue {
  static final KatakanaKeyword INSTANCE = new KatakanaKeyword();

  private KatakanaKeyword() {
    super(121, "katakana", "katakana");
  }
}
