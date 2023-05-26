package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class NoDropKeyword extends StandardKeyword implements CursorValue {
  static final NoDropKeyword INSTANCE = new NoDropKeyword();

  private NoDropKeyword() {
    super(163, "noDrop", "no-drop");
  }
}
