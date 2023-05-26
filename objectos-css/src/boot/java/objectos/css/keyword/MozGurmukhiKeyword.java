package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class MozGurmukhiKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozGurmukhiKeyword INSTANCE = new MozGurmukhiKeyword();

  private MozGurmukhiKeyword() {
    super(6, "mozGurmukhi", "-moz-gurmukhi");
  }
}
