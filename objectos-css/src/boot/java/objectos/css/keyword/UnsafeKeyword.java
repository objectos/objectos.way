package objectos.css.keyword;

import objectos.css.type.OverflowPosition;

public final class UnsafeKeyword extends StandardKeyword implements OverflowPosition {
  static final UnsafeKeyword INSTANCE = new UnsafeKeyword();

  private UnsafeKeyword() {
    super(263, "unsafe", "unsafe");
  }
}
