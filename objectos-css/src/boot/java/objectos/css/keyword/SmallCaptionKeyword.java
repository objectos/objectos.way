package objectos.css.keyword;

import objectos.css.type.SystemFontValue;

public final class SmallCaptionKeyword extends StandardKeyword implements SystemFontValue {
  static final SmallCaptionKeyword INSTANCE = new SmallCaptionKeyword();

  private SmallCaptionKeyword() {
    super(221, "smallCaption", "small-caption");
  }
}
