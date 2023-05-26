package objectos.css.keyword;

import objectos.css.type.BoxValue;

public final class PaddingBoxKeyword extends StandardKeyword implements BoxValue {
  static final PaddingBoxKeyword INSTANCE = new PaddingBoxKeyword();

  private PaddingBoxKeyword() {
    super(179, "paddingBox", "padding-box");
  }
}
