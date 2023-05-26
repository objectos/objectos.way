package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class CircleKeyword extends StandardKeyword implements CounterStyleValue {
  static final CircleKeyword INSTANCE = new CircleKeyword();

  private CircleKeyword() {
    super(41, "circle", "circle");
  }
}
