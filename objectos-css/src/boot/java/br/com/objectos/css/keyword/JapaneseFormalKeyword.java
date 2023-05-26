package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class JapaneseFormalKeyword extends StandardKeyword implements CounterStyleValue {
  static final JapaneseFormalKeyword INSTANCE = new JapaneseFormalKeyword();

  private JapaneseFormalKeyword() {
    super(117, "japaneseFormal", "japanese-formal");
  }
}
