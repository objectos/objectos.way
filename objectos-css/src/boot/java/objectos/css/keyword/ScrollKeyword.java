package objectos.css.keyword;

import objectos.css.type.BackgroundAttachmentValue;
import objectos.css.type.OverflowValue;

public final class ScrollKeyword extends StandardKeyword implements BackgroundAttachmentValue, OverflowValue {
  static final ScrollKeyword INSTANCE = new ScrollKeyword();

  private ScrollKeyword() {
    super(209, "scroll", "scroll");
  }
}
