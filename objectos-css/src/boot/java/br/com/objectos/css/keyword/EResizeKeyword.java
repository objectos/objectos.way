package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class EResizeKeyword extends StandardKeyword implements CursorValue {
  static final EResizeKeyword INSTANCE = new EResizeKeyword();

  private EResizeKeyword() {
    super(71, "eResize", "e-resize");
  }
}
