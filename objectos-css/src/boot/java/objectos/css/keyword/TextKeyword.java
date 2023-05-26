package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class TextKeyword extends StandardKeyword implements CursorValue {
  static final TextKeyword INSTANCE = new TextKeyword();

  private TextKeyword() {
    super(250, "text", "text");
  }
}
