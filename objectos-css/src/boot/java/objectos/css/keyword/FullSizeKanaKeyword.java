package objectos.css.keyword;

import objectos.css.type.TextTransformValue;

public final class FullSizeKanaKeyword extends StandardKeyword implements TextTransformValue {
  static final FullSizeKanaKeyword INSTANCE = new FullSizeKanaKeyword();

  private FullSizeKanaKeyword() {
    super(86, "fullSizeKana", "full-size-kana");
  }
}