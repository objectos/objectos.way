package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class ArmenianKeyword extends StandardKeyword implements CounterStyleValue {
  static final ArmenianKeyword INSTANCE = new ArmenianKeyword();

  private ArmenianKeyword() {
    super(21, "armenian", "armenian");
  }
}
