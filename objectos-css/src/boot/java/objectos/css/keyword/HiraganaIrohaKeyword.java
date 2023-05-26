package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class HiraganaIrohaKeyword extends StandardKeyword implements CounterStyleValue {
  static final HiraganaIrohaKeyword INSTANCE = new HiraganaIrohaKeyword();

  private HiraganaIrohaKeyword() {
    super(101, "hiraganaIroha", "hiragana-iroha");
  }
}
