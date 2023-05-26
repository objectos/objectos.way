package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class ZoomInKeyword extends StandardKeyword implements CursorValue {
  static final ZoomInKeyword INSTANCE = new ZoomInKeyword();

  private ZoomInKeyword() {
    super(283, "zoomIn", "zoom-in");
  }
}
