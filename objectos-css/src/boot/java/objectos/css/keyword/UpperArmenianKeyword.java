package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class UpperArmenianKeyword extends StandardKeyword implements CounterStyleValue {
  static final UpperArmenianKeyword INSTANCE = new UpperArmenianKeyword();

  private UpperArmenianKeyword() {
    super(266, "upperArmenian", "upper-armenian");
  }
}
