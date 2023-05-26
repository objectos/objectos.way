package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozKhmerKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozKhmerKeyword INSTANCE = new MozKhmerKeyword();

  private MozKhmerKeyword() {
    super(8, "mozKhmer", "-moz-khmer");
  }
}
