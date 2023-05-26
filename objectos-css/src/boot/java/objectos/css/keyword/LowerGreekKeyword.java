package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class LowerGreekKeyword extends StandardKeyword implements CounterStyleValue {
  static final LowerGreekKeyword INSTANCE = new LowerGreekKeyword();

  private LowerGreekKeyword() {
    super(140, "lowerGreek", "lower-greek");
  }
}
