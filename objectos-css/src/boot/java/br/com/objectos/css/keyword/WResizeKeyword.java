package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class WResizeKeyword extends StandardKeyword implements CursorValue {
  static final WResizeKeyword INSTANCE = new WResizeKeyword();

  private WResizeKeyword() {
    super(273, "wResize", "w-resize");
  }
}
