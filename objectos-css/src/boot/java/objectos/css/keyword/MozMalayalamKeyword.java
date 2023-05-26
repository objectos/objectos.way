package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class MozMalayalamKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozMalayalamKeyword INSTANCE = new MozMalayalamKeyword();

  private MozMalayalamKeyword() {
    super(10, "mozMalayalam", "-moz-malayalam");
  }
}
