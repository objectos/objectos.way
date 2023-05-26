package objectos.css.keyword;

import objectos.css.type.DisplayLegacyValue;

public final class InlineGridKeyword extends StandardKeyword implements DisplayLegacyValue {
  static final InlineGridKeyword INSTANCE = new InlineGridKeyword();

  private InlineGridKeyword() {
    super(110, "inlineGrid", "inline-grid");
  }
}
