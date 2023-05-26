package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class NResizeKeyword extends StandardKeyword implements CursorValue {
  static final NResizeKeyword INSTANCE = new NResizeKeyword();

  private NResizeKeyword() {
    super(159, "nResize", "n-resize");
  }
}
