package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class TeluguKeyword extends StandardKeyword implements CounterStyleValue {
  static final TeluguKeyword INSTANCE = new TeluguKeyword();

  private TeluguKeyword() {
    super(249, "telugu", "telugu");
  }
}
