package objectos.css.keyword;

import objectos.css.type.SystemFontValue;

public final class CaptionKeyword extends StandardKeyword implements SystemFontValue {
  static final CaptionKeyword INSTANCE = new CaptionKeyword();

  private CaptionKeyword() {
    super(37, "caption", "caption");
  }
}
