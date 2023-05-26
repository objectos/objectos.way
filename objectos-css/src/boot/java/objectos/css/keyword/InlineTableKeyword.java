package objectos.css.keyword;

import objectos.css.type.DisplayLegacyValue;

public final class InlineTableKeyword extends StandardKeyword implements DisplayLegacyValue {
  static final InlineTableKeyword INSTANCE = new InlineTableKeyword();

  private InlineTableKeyword() {
    super(112, "inlineTable", "inline-table");
  }
}
