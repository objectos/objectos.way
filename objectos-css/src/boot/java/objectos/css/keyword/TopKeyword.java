package objectos.css.keyword;

import objectos.css.type.BackgroundPositionValue;
import objectos.css.type.VerticalAlignValue;

public final class TopKeyword extends StandardKeyword implements BackgroundPositionValue, VerticalAlignValue {
  static final TopKeyword INSTANCE = new TopKeyword();

  private TopKeyword() {
    super(259, "top", "top");
  }
}
