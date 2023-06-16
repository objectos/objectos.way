package objectos.css.keyword;

import objectos.css.type.FlexBasisValue;

public final class ContentKeyword extends StandardKeyword implements FlexBasisValue {
  static final ContentKeyword INSTANCE = new ContentKeyword();

  private ContentKeyword() {
    super(53, "content", "content");
  }
}