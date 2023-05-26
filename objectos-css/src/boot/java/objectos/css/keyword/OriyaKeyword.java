package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class OriyaKeyword extends StandardKeyword implements CounterStyleValue {
  static final OriyaKeyword INSTANCE = new OriyaKeyword();

  private OriyaKeyword() {
    super(175, "oriya", "oriya");
  }
}
