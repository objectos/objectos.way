package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class VerticalTextKeyword extends StandardKeyword implements CursorValue {
  static final VerticalTextKeyword INSTANCE = new VerticalTextKeyword();

  private VerticalTextKeyword() {
    super(271, "verticalText", "vertical-text");
  }
}
