package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MongolianKeyword extends StandardKeyword implements CounterStyleValue {
  static final MongolianKeyword INSTANCE = new MongolianKeyword();

  private MongolianKeyword() {
    super(155, "mongolian", "mongolian");
  }
}
