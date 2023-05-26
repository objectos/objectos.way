package objectos.css.keyword;

import objectos.css.type.PositionValue;

public final class StickyKeyword extends StandardKeyword implements PositionValue {
  static final StickyKeyword INSTANCE = new StickyKeyword();

  private StickyKeyword() {
    super(234, "sticky", "sticky");
  }
}
