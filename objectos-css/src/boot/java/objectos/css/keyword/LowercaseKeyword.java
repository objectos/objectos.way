package objectos.css.keyword;

import objectos.css.type.TextTransformValue;

public final class LowercaseKeyword extends StandardKeyword implements TextTransformValue {
  static final LowercaseKeyword INSTANCE = new LowercaseKeyword();

  private LowercaseKeyword() {
    super(143, "lowercase", "lowercase");
  }
}
