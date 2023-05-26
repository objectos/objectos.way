package br.com.objectos.css.keyword;

import br.com.objectos.css.type.SystemFontValue;

public final class SmallCaptionKeyword extends StandardKeyword implements SystemFontValue {
  static final SmallCaptionKeyword INSTANCE = new SmallCaptionKeyword();

  private SmallCaptionKeyword() {
    super(221, "smallCaption", "small-caption");
  }
}
