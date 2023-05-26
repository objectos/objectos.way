package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class MozBengaliKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozBengaliKeyword INSTANCE = new MozBengaliKeyword();

  private MozBengaliKeyword() {
    super(1, "mozBengali", "-moz-bengali");
  }
}
