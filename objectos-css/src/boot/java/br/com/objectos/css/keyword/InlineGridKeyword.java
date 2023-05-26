package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayLegacyValue;

public final class InlineGridKeyword extends StandardKeyword implements DisplayLegacyValue {
  static final InlineGridKeyword INSTANCE = new InlineGridKeyword();

  private InlineGridKeyword() {
    super(110, "inlineGrid", "inline-grid");
  }
}
