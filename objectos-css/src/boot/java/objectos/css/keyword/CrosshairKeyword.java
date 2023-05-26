package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class CrosshairKeyword extends StandardKeyword implements CursorValue {
  static final CrosshairKeyword INSTANCE = new CrosshairKeyword();

  private CrosshairKeyword() {
    super(59, "crosshair", "crosshair");
  }
}
