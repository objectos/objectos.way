package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozGujaratiKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozGujaratiKeyword INSTANCE = new MozGujaratiKeyword();

  private MozGujaratiKeyword() {
    super(5, "mozGujarati", "-moz-gujarati");
  }
}
