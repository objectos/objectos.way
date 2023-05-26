package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozGurmukhiKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozGurmukhiKeyword INSTANCE = new MozGurmukhiKeyword();

  private MozGurmukhiKeyword() {
    super(6, "mozGurmukhi", "-moz-gurmukhi");
  }
}
