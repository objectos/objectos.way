package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CursorValue;

public final class NeswResizeKeyword extends StandardKeyword implements CursorValue {
  static final NeswResizeKeyword INSTANCE = new NeswResizeKeyword();

  private NeswResizeKeyword() {
    super(161, "neswResize", "nesw-resize");
  }
}
