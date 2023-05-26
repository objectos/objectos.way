package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class SquareKeyword extends StandardKeyword implements CounterStyleValue {
  static final SquareKeyword INSTANCE = new SquareKeyword();

  private SquareKeyword() {
    super(229, "square", "square");
  }
}
