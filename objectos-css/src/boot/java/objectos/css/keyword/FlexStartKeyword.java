package objectos.css.keyword;

import objectos.css.type.ContentPosition;
import objectos.css.type.SelfPosition;

public final class FlexStartKeyword extends StandardKeyword implements ContentPosition, SelfPosition {
  static final FlexStartKeyword INSTANCE = new FlexStartKeyword();

  private FlexStartKeyword() {
    super(82, "flexStart", "flex-start");
  }
}
