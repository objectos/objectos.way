package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class GurmukhiKeyword extends StandardKeyword implements CounterStyleValue {
  static final GurmukhiKeyword INSTANCE = new GurmukhiKeyword();

  private GurmukhiKeyword() {
    super(95, "gurmukhi", "gurmukhi");
  }
}
