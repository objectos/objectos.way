package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class GujaratiKeyword extends StandardKeyword implements CounterStyleValue {
  static final GujaratiKeyword INSTANCE = new GujaratiKeyword();

  private GujaratiKeyword() {
    super(94, "gujarati", "gujarati");
  }
}
