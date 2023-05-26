package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class KatakanaIrohaKeyword extends StandardKeyword implements CounterStyleValue {
  static final KatakanaIrohaKeyword INSTANCE = new KatakanaIrohaKeyword();

  private KatakanaIrohaKeyword() {
    super(122, "katakanaIroha", "katakana-iroha");
  }
}
