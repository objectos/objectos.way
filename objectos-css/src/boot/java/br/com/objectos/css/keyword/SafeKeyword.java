package br.com.objectos.css.keyword;

import br.com.objectos.css.type.OverflowPosition;

public final class SafeKeyword extends StandardKeyword implements OverflowPosition {
  static final SafeKeyword INSTANCE = new SafeKeyword();

  private SafeKeyword() {
    super(206, "safe", "safe");
  }
}
