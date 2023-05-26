package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class WaitKeyword extends StandardKeyword implements CursorValue {
  static final WaitKeyword INSTANCE = new WaitKeyword();

  private WaitKeyword() {
    super(274, "wait", "wait");
  }
}
