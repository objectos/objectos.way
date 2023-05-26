package objectos.css.keyword;

import objectos.css.type.ContentPosition;
import objectos.css.type.SelfPosition;

public final class FlexEndKeyword extends StandardKeyword implements ContentPosition, SelfPosition {
  static final FlexEndKeyword INSTANCE = new FlexEndKeyword();

  private FlexEndKeyword() {
    super(81, "flexEnd", "flex-end");
  }
}
