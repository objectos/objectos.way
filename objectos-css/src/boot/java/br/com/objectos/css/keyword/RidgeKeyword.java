package br.com.objectos.css.keyword;

import br.com.objectos.css.type.LineStyleValue;

public final class RidgeKeyword extends StandardKeyword implements LineStyleValue {
  static final RidgeKeyword INSTANCE = new RidgeKeyword();

  private RidgeKeyword() {
    super(193, "ridge", "ridge");
  }
}
