package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayLegacyValue;

public final class InlineFlexKeyword extends StandardKeyword implements DisplayLegacyValue {
  static final InlineFlexKeyword INSTANCE = new InlineFlexKeyword();

  private InlineFlexKeyword() {
    super(109, "inlineFlex", "inline-flex");
  }
}
