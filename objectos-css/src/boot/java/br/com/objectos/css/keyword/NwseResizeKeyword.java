package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class NwseResizeKeyword extends StandardKeyword implements CursorValue {
  static final NwseResizeKeyword INSTANCE = new NwseResizeKeyword();

  private NwseResizeKeyword() {
    super(172, "nwseResize", "nwse-resize");
  }
}
