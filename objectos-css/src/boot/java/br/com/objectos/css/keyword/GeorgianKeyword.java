package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class GeorgianKeyword extends StandardKeyword implements CounterStyleValue {
  static final GeorgianKeyword INSTANCE = new GeorgianKeyword();

  private GeorgianKeyword() {
    super(88, "georgian", "georgian");
  }
}
