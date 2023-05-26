package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class ColResizeKeyword extends StandardKeyword implements CursorValue {
  static final ColResizeKeyword INSTANCE = new ColResizeKeyword();

  private ColResizeKeyword() {
    super(48, "colResize", "col-resize");
  }
}
