package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class PointerKeyword extends StandardKeyword implements CursorValue {
  static final PointerKeyword INSTANCE = new PointerKeyword();

  private PointerKeyword() {
    super(181, "pointer", "pointer");
  }
}
