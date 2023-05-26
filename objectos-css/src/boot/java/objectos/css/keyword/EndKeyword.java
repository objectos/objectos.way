package objectos.css.keyword;

import objectos.css.type.ContentPosition;
import objectos.css.type.SelfPosition;
import objectos.css.type.TextAlignValue;

public final class EndKeyword extends StandardKeyword implements ContentPosition, SelfPosition, TextAlignValue {
  static final EndKeyword INSTANCE = new EndKeyword();

  private EndKeyword() {
    super(73, "end", "end");
  }
}
