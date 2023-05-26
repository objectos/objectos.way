package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class SResizeKeyword extends StandardKeyword implements CursorValue {
  static final SResizeKeyword INSTANCE = new SResizeKeyword();

  private SResizeKeyword() {
    super(205, "sResize", "s-resize");
  }
}
