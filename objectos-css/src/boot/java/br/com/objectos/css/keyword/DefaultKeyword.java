package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class DefaultKeyword extends StandardKeyword implements CursorValue {
  static final DefaultKeyword INSTANCE = new DefaultKeyword();

  private DefaultKeyword() {
    super(64, "defaultKw", "default");
  }
}
