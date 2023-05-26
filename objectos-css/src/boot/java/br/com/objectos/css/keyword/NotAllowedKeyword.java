package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class NotAllowedKeyword extends StandardKeyword implements CursorValue {
  static final NotAllowedKeyword INSTANCE = new NotAllowedKeyword();

  private NotAllowedKeyword() {
    super(168, "notAllowed", "not-allowed");
  }
}
