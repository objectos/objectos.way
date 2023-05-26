package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class LaoKeyword extends StandardKeyword implements CounterStyleValue {
  static final LaoKeyword INSTANCE = new LaoKeyword();

  private LaoKeyword() {
    super(127, "lao", "lao");
  }
}
