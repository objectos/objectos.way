package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class NResizeKeyword extends StandardKeyword implements CursorValue {
  static final NResizeKeyword INSTANCE = new NResizeKeyword();

  private NResizeKeyword() {
    super(159, "nResize", "n-resize");
  }
}
