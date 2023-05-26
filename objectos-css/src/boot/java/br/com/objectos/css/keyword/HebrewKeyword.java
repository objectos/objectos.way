package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class HebrewKeyword extends StandardKeyword implements CounterStyleValue {
  static final HebrewKeyword INSTANCE = new HebrewKeyword();

  private HebrewKeyword() {
    super(97, "hebrew", "hebrew");
  }
}
