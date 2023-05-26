package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BackgroundAttachmentValue;
import br.com.objectos.css.type.PositionValue;

public final class FixedKeyword extends StandardKeyword implements BackgroundAttachmentValue, PositionValue {
  static final FixedKeyword INSTANCE = new FixedKeyword();

  private FixedKeyword() {
    super(79, "fixed", "fixed");
  }
}
