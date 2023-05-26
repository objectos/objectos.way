package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozKannadaKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozKannadaKeyword INSTANCE = new MozKannadaKeyword();

  private MozKannadaKeyword() {
    super(7, "mozKannada", "-moz-kannada");
  }
}
