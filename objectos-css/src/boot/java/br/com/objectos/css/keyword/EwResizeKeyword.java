package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class EwResizeKeyword extends StandardKeyword implements CursorValue {
  static final EwResizeKeyword INSTANCE = new EwResizeKeyword();

  private EwResizeKeyword() {
    super(75, "ewResize", "ew-resize");
  }
}
