package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class WaitKeyword extends StandardKeyword implements CursorValue {
  static final WaitKeyword INSTANCE = new WaitKeyword();

  private WaitKeyword() {
    super(274, "wait", "wait");
  }
}
