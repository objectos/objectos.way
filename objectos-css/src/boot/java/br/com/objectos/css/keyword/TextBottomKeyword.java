package br.com.objectos.css.keyword;

import br.com.objectos.css.type.VerticalAlignValue;

public final class TextBottomKeyword extends StandardKeyword implements VerticalAlignValue {
  static final TextBottomKeyword INSTANCE = new TextBottomKeyword();

  private TextBottomKeyword() {
    super(251, "textBottom", "text-bottom");
  }
}
