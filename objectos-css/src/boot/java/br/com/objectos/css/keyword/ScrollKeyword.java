package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BackgroundAttachmentValue;
import br.com.objectos.css.type.OverflowValue;

public final class ScrollKeyword extends StandardKeyword implements BackgroundAttachmentValue, OverflowValue {
  static final ScrollKeyword INSTANCE = new ScrollKeyword();

  private ScrollKeyword() {
    super(209, "scroll", "scroll");
  }
}
