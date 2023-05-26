package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class KatakanaKeyword extends StandardKeyword implements CounterStyleValue {
  static final KatakanaKeyword INSTANCE = new KatakanaKeyword();

  private KatakanaKeyword() {
    super(121, "katakana", "katakana");
  }
}
