package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozDevanagariKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozDevanagariKeyword INSTANCE = new MozDevanagariKeyword();

  private MozDevanagariKeyword() {
    super(4, "mozDevanagari", "-moz-devanagari");
  }
}
