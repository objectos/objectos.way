package objectos.css.keyword;

import objectos.css.type.OverflowPosition;

public final class SafeKeyword extends StandardKeyword implements OverflowPosition {
  static final SafeKeyword INSTANCE = new SafeKeyword();

  private SafeKeyword() {
    super(206, "safe", "safe");
  }
}
