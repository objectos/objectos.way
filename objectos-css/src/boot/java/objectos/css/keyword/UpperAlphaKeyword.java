package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class UpperAlphaKeyword extends StandardKeyword implements CounterStyleValue {
  static final UpperAlphaKeyword INSTANCE = new UpperAlphaKeyword();

  private UpperAlphaKeyword() {
    super(265, "upperAlpha", "upper-alpha");
  }
}
