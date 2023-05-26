package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class PointerKeyword extends StandardKeyword implements CursorValue {
  static final PointerKeyword INSTANCE = new PointerKeyword();

  private PointerKeyword() {
    super(181, "pointer", "pointer");
  }
}
