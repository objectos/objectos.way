package br.com.objectos.css.keyword;

import br.com.objectos.css.type.VerticalAlignValue;

public final class TextTopKeyword extends StandardKeyword implements VerticalAlignValue {
  static final TextTopKeyword INSTANCE = new TextTopKeyword();

  private TextTopKeyword() {
    super(252, "textTop", "text-top");
  }
}
