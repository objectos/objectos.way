package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class HebrewKeyword extends StandardKeyword implements CounterStyleValue {
  static final HebrewKeyword INSTANCE = new HebrewKeyword();

  private HebrewKeyword() {
    super(97, "hebrew", "hebrew");
  }
}
