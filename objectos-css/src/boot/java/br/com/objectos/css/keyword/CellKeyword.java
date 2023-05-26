package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class CellKeyword extends StandardKeyword implements CursorValue {
  static final CellKeyword INSTANCE = new CellKeyword();

  private CellKeyword() {
    super(38, "cell", "cell");
  }
}
