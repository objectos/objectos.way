package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class JapaneseInformalKeyword extends StandardKeyword implements CounterStyleValue {
  static final JapaneseInformalKeyword INSTANCE = new JapaneseInformalKeyword();

  private JapaneseInformalKeyword() {
    super(118, "japaneseInformal", "japanese-informal");
  }
}
