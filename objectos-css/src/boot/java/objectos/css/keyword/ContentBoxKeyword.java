package objectos.css.keyword;

import objectos.css.type.BoxSizingValue;
import objectos.css.type.BoxValue;

public final class ContentBoxKeyword extends StandardKeyword implements BoxSizingValue, BoxValue {
  static final ContentBoxKeyword INSTANCE = new ContentBoxKeyword();

  private ContentBoxKeyword() {
    super(54, "contentBox", "content-box");
  }
}
