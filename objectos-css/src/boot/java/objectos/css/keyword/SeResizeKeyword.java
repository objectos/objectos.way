package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class SeResizeKeyword extends StandardKeyword implements CursorValue {
  static final SeResizeKeyword INSTANCE = new SeResizeKeyword();

  private SeResizeKeyword() {
    super(210, "seResize", "se-resize");
  }
}
