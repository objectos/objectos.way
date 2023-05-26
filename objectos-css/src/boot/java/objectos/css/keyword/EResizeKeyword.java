package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class EResizeKeyword extends StandardKeyword implements CursorValue {
  static final EResizeKeyword INSTANCE = new EResizeKeyword();

  private EResizeKeyword() {
    super(71, "eResize", "e-resize");
  }
}
