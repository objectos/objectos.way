package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozPersianKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozPersianKeyword INSTANCE = new MozPersianKeyword();

  private MozPersianKeyword() {
    super(13, "mozPersian", "-moz-persian");
  }
}
