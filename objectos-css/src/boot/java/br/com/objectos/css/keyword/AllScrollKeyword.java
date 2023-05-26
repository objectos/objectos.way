package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class AllScrollKeyword extends StandardKeyword implements CursorValue {
  static final AllScrollKeyword INSTANCE = new AllScrollKeyword();

  private AllScrollKeyword() {
    super(19, "allScroll", "all-scroll");
  }
}
