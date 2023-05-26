package objectos.css.keyword;

import objectos.css.type.JustifyItemsValue;

public final class LegacyKeyword extends StandardKeyword implements JustifyItemsValue {
  static final LegacyKeyword INSTANCE = new LegacyKeyword();

  private LegacyKeyword() {
    super(132, "legacy", "legacy");
  }
}
