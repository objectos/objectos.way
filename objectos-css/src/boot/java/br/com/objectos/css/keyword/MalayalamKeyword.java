package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MalayalamKeyword extends StandardKeyword implements CounterStyleValue {
  static final MalayalamKeyword INSTANCE = new MalayalamKeyword();

  private MalayalamKeyword() {
    super(144, "malayalam", "malayalam");
  }
}
