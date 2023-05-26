package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class KhmerKeyword extends StandardKeyword implements CounterStyleValue {
  static final KhmerKeyword INSTANCE = new KhmerKeyword();

  private KhmerKeyword() {
    super(123, "khmer", "khmer");
  }
}
