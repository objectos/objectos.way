package objectos.css.keyword;

import objectos.css.type.ContentPosition;
import objectos.css.type.SelfPosition;
import objectos.css.type.TextAlignValue;

public final class StartKeyword extends StandardKeyword implements ContentPosition, SelfPosition, TextAlignValue {
  static final StartKeyword INSTANCE = new StartKeyword();

  private StartKeyword() {
    super(231, "start", "start");
  }
}
