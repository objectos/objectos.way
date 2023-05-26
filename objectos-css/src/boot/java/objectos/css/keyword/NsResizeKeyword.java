package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class NsResizeKeyword extends StandardKeyword implements CursorValue {
  static final NsResizeKeyword INSTANCE = new NsResizeKeyword();

  private NsResizeKeyword() {
    super(170, "nsResize", "ns-resize");
  }
}
