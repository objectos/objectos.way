package objectos.css.keyword;

import objectos.css.type.FontVariantCss21Value;

public final class SmallCapsKeyword extends StandardKeyword implements FontVariantCss21Value {
  static final SmallCapsKeyword INSTANCE = new SmallCapsKeyword();

  private SmallCapsKeyword() {
    super(220, "smallCaps", "small-caps");
  }
}
