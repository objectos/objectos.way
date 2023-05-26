package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozMyanmarKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozMyanmarKeyword INSTANCE = new MozMyanmarKeyword();

  private MozMyanmarKeyword() {
    super(11, "mozMyanmar", "-moz-myanmar");
  }
}
