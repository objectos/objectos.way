package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class MoveKeyword extends StandardKeyword implements CursorValue {
  static final MoveKeyword INSTANCE = new MoveKeyword();

  private MoveKeyword() {
    super(157, "move", "move");
  }
}
