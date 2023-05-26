package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozTeluguKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozTeluguKeyword INSTANCE = new MozTeluguKeyword();

  private MozTeluguKeyword() {
    super(15, "mozTelugu", "-moz-telugu");
  }
}
