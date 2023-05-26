package br.com.objectos.css.keyword;

import br.com.objectos.css.type.VerticalAlignValue;

public final class MiddleKeyword extends StandardKeyword implements VerticalAlignValue {
  static final MiddleKeyword INSTANCE = new MiddleKeyword();

  private MiddleKeyword() {
    super(153, "middle", "middle");
  }
}
