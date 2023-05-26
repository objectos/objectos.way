package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class NwseResizeKeyword extends StandardKeyword implements CursorValue {
  static final NwseResizeKeyword INSTANCE = new NwseResizeKeyword();

  private NwseResizeKeyword() {
    super(172, "nwseResize", "nwse-resize");
  }
}
