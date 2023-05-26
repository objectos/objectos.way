package br.com.objectos.css.keyword;

import br.com.objectos.css.type.TextDecorationLineKind;

public final class BlinkKeyword extends StandardKeyword implements TextDecorationLineKind {
  static final BlinkKeyword INSTANCE = new BlinkKeyword();

  private BlinkKeyword() {
    super(25, "blink", "blink");
  }
}
