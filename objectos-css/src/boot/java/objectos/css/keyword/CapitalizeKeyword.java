package objectos.css.keyword;

import objectos.css.type.TextTransformValue;

public final class CapitalizeKeyword extends StandardKeyword implements TextTransformValue {
  static final CapitalizeKeyword INSTANCE = new CapitalizeKeyword();

  private CapitalizeKeyword() {
    super(36, "capitalize", "capitalize");
  }
}
