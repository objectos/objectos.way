package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class KhmerKeyword extends StandardKeyword implements CounterStyleValue {
  static final KhmerKeyword INSTANCE = new KhmerKeyword();

  private KhmerKeyword() {
    super(123, "khmer", "khmer");
  }
}
