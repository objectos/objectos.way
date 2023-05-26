package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class MozPersianKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozPersianKeyword INSTANCE = new MozPersianKeyword();

  private MozPersianKeyword() {
    super(13, "mozPersian", "-moz-persian");
  }
}
