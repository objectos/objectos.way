package objectos.css.keyword;

import objectos.css.type.TextTransformValue;

public final class UppercaseKeyword extends StandardKeyword implements TextTransformValue {
  static final UppercaseKeyword INSTANCE = new UppercaseKeyword();

  private UppercaseKeyword() {
    super(269, "uppercase", "uppercase");
  }
}
