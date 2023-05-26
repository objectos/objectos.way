package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class KatakanaIrohaKeyword extends StandardKeyword implements CounterStyleValue {
  static final KatakanaIrohaKeyword INSTANCE = new KatakanaIrohaKeyword();

  private KatakanaIrohaKeyword() {
    super(122, "katakanaIroha", "katakana-iroha");
  }
}
