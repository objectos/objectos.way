package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class KannadaKeyword extends StandardKeyword implements CounterStyleValue {
  static final KannadaKeyword INSTANCE = new KannadaKeyword();

  private KannadaKeyword() {
    super(120, "kannada", "kannada");
  }
}
