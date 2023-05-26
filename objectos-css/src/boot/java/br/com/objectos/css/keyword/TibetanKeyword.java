package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class TibetanKeyword extends StandardKeyword implements CounterStyleValue {
  static final TibetanKeyword INSTANCE = new TibetanKeyword();

  private TibetanKeyword() {
    super(258, "tibetan", "tibetan");
  }
}
