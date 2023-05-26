package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class RowResizeKeyword extends StandardKeyword implements CursorValue {
  static final RowResizeKeyword INSTANCE = new RowResizeKeyword();

  private RowResizeKeyword() {
    super(197, "rowResize", "row-resize");
  }
}
