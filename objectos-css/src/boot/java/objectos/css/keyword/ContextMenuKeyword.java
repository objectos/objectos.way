package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class ContextMenuKeyword extends StandardKeyword implements CursorValue {
  static final ContextMenuKeyword INSTANCE = new ContextMenuKeyword();

  private ContextMenuKeyword() {
    super(56, "contextMenu", "context-menu");
  }
}
