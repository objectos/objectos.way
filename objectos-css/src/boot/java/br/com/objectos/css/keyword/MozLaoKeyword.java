package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozLaoKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozLaoKeyword INSTANCE = new MozLaoKeyword();

  private MozLaoKeyword() {
    super(9, "mozLao", "-moz-lao");
  }
}
