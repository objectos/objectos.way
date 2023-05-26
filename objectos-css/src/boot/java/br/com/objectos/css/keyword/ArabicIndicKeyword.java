package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class ArabicIndicKeyword extends StandardKeyword implements CounterStyleValue {
  static final ArabicIndicKeyword INSTANCE = new ArabicIndicKeyword();

  private ArabicIndicKeyword() {
    super(20, "arabicIndic", "arabic-indic");
  }
}
