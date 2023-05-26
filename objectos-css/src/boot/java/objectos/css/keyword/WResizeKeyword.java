package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class WResizeKeyword extends StandardKeyword implements CursorValue {
  static final WResizeKeyword INSTANCE = new WResizeKeyword();

  private WResizeKeyword() {
    super(273, "wResize", "w-resize");
  }
}
