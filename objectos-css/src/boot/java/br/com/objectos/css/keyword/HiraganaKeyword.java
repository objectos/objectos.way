package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class HiraganaKeyword extends StandardKeyword implements CounterStyleValue {
  static final HiraganaKeyword INSTANCE = new HiraganaKeyword();

  private HiraganaKeyword() {
    super(100, "hiragana", "hiragana");
  }
}
