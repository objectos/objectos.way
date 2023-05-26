package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class MozTamilKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozTamilKeyword INSTANCE = new MozTamilKeyword();

  private MozTamilKeyword() {
    super(14, "mozTamil", "-moz-tamil");
  }
}
