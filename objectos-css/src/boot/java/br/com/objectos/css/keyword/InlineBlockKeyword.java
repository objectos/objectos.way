package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayLegacyValue;

public final class InlineBlockKeyword extends StandardKeyword implements DisplayLegacyValue {
  static final InlineBlockKeyword INSTANCE = new InlineBlockKeyword();

  private InlineBlockKeyword() {
    super(107, "inlineBlock", "inline-block");
  }
}
