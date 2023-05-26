package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class AliasKeyword extends StandardKeyword implements CursorValue {
  static final AliasKeyword INSTANCE = new AliasKeyword();

  private AliasKeyword() {
    super(18, "alias", "alias");
  }
}
