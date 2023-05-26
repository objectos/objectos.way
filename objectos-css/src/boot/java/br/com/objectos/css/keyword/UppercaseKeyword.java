package br.com.objectos.css.keyword;

import br.com.objectos.css.type.TextTransformValue;

public final class UppercaseKeyword extends StandardKeyword implements TextTransformValue {
  static final UppercaseKeyword INSTANCE = new UppercaseKeyword();

  private UppercaseKeyword() {
    super(269, "uppercase", "uppercase");
  }
}
