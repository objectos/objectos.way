package br.com.objectos.css.keyword;

import br.com.objectos.css.type.SystemFontValue;

public final class CaptionKeyword extends StandardKeyword implements SystemFontValue {
  static final CaptionKeyword INSTANCE = new CaptionKeyword();

  private CaptionKeyword() {
    super(37, "caption", "caption");
  }
}
