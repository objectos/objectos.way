package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class MozCjkHeavenlyStemKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozCjkHeavenlyStemKeyword INSTANCE = new MozCjkHeavenlyStemKeyword();

  private MozCjkHeavenlyStemKeyword() {
    super(3, "mozCjkHeavenlyStem", "-moz-cjk-heavenly-stem");
  }
}
