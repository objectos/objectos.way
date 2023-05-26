package br.com.objectos.css.keyword;

import br.com.objectos.css.type.TextTransformValue;

public final class LowercaseKeyword extends StandardKeyword implements TextTransformValue {
  static final LowercaseKeyword INSTANCE = new LowercaseKeyword();

  private LowercaseKeyword() {
    super(143, "lowercase", "lowercase");
  }
}
