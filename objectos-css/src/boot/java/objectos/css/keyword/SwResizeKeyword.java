package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class SwResizeKeyword extends StandardKeyword implements CursorValue {
  static final SwResizeKeyword INSTANCE = new SwResizeKeyword();

  private SwResizeKeyword() {
    super(238, "swResize", "sw-resize");
  }
}
