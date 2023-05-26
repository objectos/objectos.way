package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class ProgressKeyword extends StandardKeyword implements CursorValue {
  static final ProgressKeyword INSTANCE = new ProgressKeyword();

  private ProgressKeyword() {
    super(185, "progressKw", "progress");
  }
}
