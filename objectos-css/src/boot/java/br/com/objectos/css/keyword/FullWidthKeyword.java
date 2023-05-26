package br.com.objectos.css.keyword;

import br.com.objectos.css.type.TextTransformValue;

public final class FullWidthKeyword extends StandardKeyword implements TextTransformValue {
  static final FullWidthKeyword INSTANCE = new FullWidthKeyword();

  private FullWidthKeyword() {
    super(87, "fullWidth", "full-width");
  }
}
