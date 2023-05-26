package objectos.css.keyword;

import objectos.css.type.DisplayLegacyValue;

public final class InlineFlexKeyword extends StandardKeyword implements DisplayLegacyValue {
  static final InlineFlexKeyword INSTANCE = new InlineFlexKeyword();

  private InlineFlexKeyword() {
    super(109, "inlineFlex", "inline-flex");
  }
}
