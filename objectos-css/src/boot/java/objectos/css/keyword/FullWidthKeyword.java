package objectos.css.keyword;

import objectos.css.type.TextTransformValue;

public final class FullWidthKeyword extends StandardKeyword implements TextTransformValue {
  static final FullWidthKeyword INSTANCE = new FullWidthKeyword();

  private FullWidthKeyword() {
    super(87, "fullWidth", "full-width");
  }
}
