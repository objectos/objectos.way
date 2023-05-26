package br.com.objectos.css.keyword;

import br.com.objectos.css.type.OverflowPosition;

public final class UnsafeKeyword extends StandardKeyword implements OverflowPosition {
  static final UnsafeKeyword INSTANCE = new UnsafeKeyword();

  private UnsafeKeyword() {
    super(263, "unsafe", "unsafe");
  }
}
