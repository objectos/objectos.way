package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class TamilKeyword extends StandardKeyword implements CounterStyleValue {
  static final TamilKeyword INSTANCE = new TamilKeyword();

  private TamilKeyword() {
    super(248, "tamil", "tamil");
  }
}
