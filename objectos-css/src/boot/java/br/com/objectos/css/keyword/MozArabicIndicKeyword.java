package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozArabicIndicKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozArabicIndicKeyword INSTANCE = new MozArabicIndicKeyword();

  private MozArabicIndicKeyword() {
    super(0, "mozArabicIndic", "-moz-arabic-indic");
  }
}
