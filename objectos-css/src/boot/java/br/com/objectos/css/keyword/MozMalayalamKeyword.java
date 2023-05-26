package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozMalayalamKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozMalayalamKeyword INSTANCE = new MozMalayalamKeyword();

  private MozMalayalamKeyword() {
    super(10, "mozMalayalam", "-moz-malayalam");
  }
}
