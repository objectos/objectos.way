package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozOriyaKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozOriyaKeyword INSTANCE = new MozOriyaKeyword();

  private MozOriyaKeyword() {
    super(12, "mozOriya", "-moz-oriya");
  }
}
