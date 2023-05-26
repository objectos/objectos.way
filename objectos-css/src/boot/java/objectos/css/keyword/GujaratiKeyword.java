package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class GujaratiKeyword extends StandardKeyword implements CounterStyleValue {
  static final GujaratiKeyword INSTANCE = new GujaratiKeyword();

  private GujaratiKeyword() {
    super(94, "gujarati", "gujarati");
  }
}
