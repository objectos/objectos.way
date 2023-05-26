package objectos.css.keyword;

import objectos.css.type.ResizeValue;

public final class HorizontalKeyword extends StandardKeyword implements ResizeValue {
  static final HorizontalKeyword INSTANCE = new HorizontalKeyword();

  private HorizontalKeyword() {
    super(102, "horizontal", "horizontal");
  }
}
