package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class LowerArmenianKeyword extends StandardKeyword implements CounterStyleValue {
  static final LowerArmenianKeyword INSTANCE = new LowerArmenianKeyword();

  private LowerArmenianKeyword() {
    super(139, "lowerArmenian", "lower-armenian");
  }
}
