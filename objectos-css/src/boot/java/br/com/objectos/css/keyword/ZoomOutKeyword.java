package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class ZoomOutKeyword extends StandardKeyword implements CursorValue {
  static final ZoomOutKeyword INSTANCE = new ZoomOutKeyword();

  private ZoomOutKeyword() {
    super(284, "zoomOut", "zoom-out");
  }
}
