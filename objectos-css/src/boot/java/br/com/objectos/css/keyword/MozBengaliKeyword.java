package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozBengaliKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozBengaliKeyword INSTANCE = new MozBengaliKeyword();

  private MozBengaliKeyword() {
    super(1, "mozBengali", "-moz-bengali");
  }
}
