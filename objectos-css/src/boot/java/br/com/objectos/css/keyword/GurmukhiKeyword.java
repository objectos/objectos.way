package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class GurmukhiKeyword extends StandardKeyword implements CounterStyleValue {
  static final GurmukhiKeyword INSTANCE = new GurmukhiKeyword();

  private GurmukhiKeyword() {
    super(95, "gurmukhi", "gurmukhi");
  }
}
