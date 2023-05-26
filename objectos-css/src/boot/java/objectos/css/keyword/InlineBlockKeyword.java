package objectos.css.keyword;

import objectos.css.type.DisplayLegacyValue;

public final class InlineBlockKeyword extends StandardKeyword implements DisplayLegacyValue {
  static final InlineBlockKeyword INSTANCE = new InlineBlockKeyword();

  private InlineBlockKeyword() {
    super(107, "inlineBlock", "inline-block");
  }
}
