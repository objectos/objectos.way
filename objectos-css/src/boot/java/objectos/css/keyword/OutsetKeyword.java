package objectos.css.keyword;

import objectos.css.type.LineStyleValue;

public final class OutsetKeyword extends StandardKeyword implements LineStyleValue {
  static final OutsetKeyword INSTANCE = new OutsetKeyword();

  private OutsetKeyword() {
    super(176, "outset", "outset");
  }
}
