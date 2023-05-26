package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class ThaiKeyword extends StandardKeyword implements CounterStyleValue {
  static final ThaiKeyword INSTANCE = new ThaiKeyword();

  private ThaiKeyword() {
    super(255, "thai", "thai");
  }
}
