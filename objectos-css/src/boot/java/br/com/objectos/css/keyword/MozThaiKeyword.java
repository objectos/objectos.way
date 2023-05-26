package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozThaiKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozThaiKeyword INSTANCE = new MozThaiKeyword();

  private MozThaiKeyword() {
    super(16, "mozThai", "-moz-thai");
  }
}
