package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class MozOriyaKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozOriyaKeyword INSTANCE = new MozOriyaKeyword();

  private MozOriyaKeyword() {
    super(12, "mozOriya", "-moz-oriya");
  }
}
