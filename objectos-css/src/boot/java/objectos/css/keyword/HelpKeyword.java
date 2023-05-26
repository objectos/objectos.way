package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class HelpKeyword extends StandardKeyword implements CursorValue {
  static final HelpKeyword INSTANCE = new HelpKeyword();

  private HelpKeyword() {
    super(98, "help", "help");
  }
}
