package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class NeResizeKeyword extends StandardKeyword implements CursorValue {
  static final NeResizeKeyword INSTANCE = new NeResizeKeyword();

  private NeResizeKeyword() {
    super(160, "neResize", "ne-resize");
  }
}
