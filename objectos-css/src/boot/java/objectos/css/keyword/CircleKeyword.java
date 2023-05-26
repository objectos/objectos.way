package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class CircleKeyword extends StandardKeyword implements CounterStyleValue {
  static final CircleKeyword INSTANCE = new CircleKeyword();

  private CircleKeyword() {
    super(41, "circle", "circle");
  }
}
