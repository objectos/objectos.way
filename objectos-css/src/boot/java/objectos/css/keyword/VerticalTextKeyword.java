package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class VerticalTextKeyword extends StandardKeyword implements CursorValue {
  static final VerticalTextKeyword INSTANCE = new VerticalTextKeyword();

  private VerticalTextKeyword() {
    super(271, "verticalText", "vertical-text");
  }
}
