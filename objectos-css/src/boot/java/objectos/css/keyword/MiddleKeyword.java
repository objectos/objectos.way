package objectos.css.keyword;

import objectos.css.type.VerticalAlignValue;

public final class MiddleKeyword extends StandardKeyword implements VerticalAlignValue {
  static final MiddleKeyword INSTANCE = new MiddleKeyword();

  private MiddleKeyword() {
    super(153, "middle", "middle");
  }
}
