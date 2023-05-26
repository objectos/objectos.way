package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class CjkHeavenlyStemKeyword extends StandardKeyword implements CounterStyleValue {
  static final CjkHeavenlyStemKeyword INSTANCE = new CjkHeavenlyStemKeyword();

  private CjkHeavenlyStemKeyword() {
    super(44, "cjkHeavenlyStem", "cjk-heavenly-stem");
  }
}
