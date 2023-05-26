package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class KannadaKeyword extends StandardKeyword implements CounterStyleValue {
  static final KannadaKeyword INSTANCE = new KannadaKeyword();

  private KannadaKeyword() {
    super(120, "kannada", "kannada");
  }
}
