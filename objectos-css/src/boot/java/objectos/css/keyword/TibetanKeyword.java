package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class TibetanKeyword extends StandardKeyword implements CounterStyleValue {
  static final TibetanKeyword INSTANCE = new TibetanKeyword();

  private TibetanKeyword() {
    super(258, "tibetan", "tibetan");
  }
}
