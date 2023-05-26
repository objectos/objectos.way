package objectos.css.keyword;

import objectos.css.type.ResizeValue;

public final class VerticalKeyword extends StandardKeyword implements ResizeValue {
  static final VerticalKeyword INSTANCE = new VerticalKeyword();

  private VerticalKeyword() {
    super(270, "vertical", "vertical");
  }
}
