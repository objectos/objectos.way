package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class DiscKeyword extends StandardKeyword implements CounterStyleValue {
  static final DiscKeyword INSTANCE = new DiscKeyword();

  private DiscKeyword() {
    super(66, "disc", "disc");
  }
}
