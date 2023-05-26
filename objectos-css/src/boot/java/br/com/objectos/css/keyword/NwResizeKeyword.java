package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class NwResizeKeyword extends StandardKeyword implements CursorValue {
  static final NwResizeKeyword INSTANCE = new NwResizeKeyword();

  private NwResizeKeyword() {
    super(171, "nwResize", "nw-resize");
  }
}
