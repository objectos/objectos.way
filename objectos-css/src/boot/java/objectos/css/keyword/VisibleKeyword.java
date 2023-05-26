package objectos.css.keyword;

import objectos.css.type.OverflowValue;

public final class VisibleKeyword extends StandardKeyword implements OverflowValue {
  static final VisibleKeyword INSTANCE = new VisibleKeyword();

  private VisibleKeyword() {
    super(272, "visible", "visible");
  }
}
