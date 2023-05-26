package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class DiscKeyword extends StandardKeyword implements CounterStyleValue {
  static final DiscKeyword INSTANCE = new DiscKeyword();

  private DiscKeyword() {
    super(66, "disc", "disc");
  }
}
