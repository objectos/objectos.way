package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class ColResizeKeyword extends StandardKeyword implements CursorValue {
  static final ColResizeKeyword INSTANCE = new ColResizeKeyword();

  private ColResizeKeyword() {
    super(48, "colResize", "col-resize");
  }
}
