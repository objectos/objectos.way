package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MyanmarKeyword extends StandardKeyword implements CounterStyleValue {
  static final MyanmarKeyword INSTANCE = new MyanmarKeyword();

  private MyanmarKeyword() {
    super(158, "myanmar", "myanmar");
  }
}
