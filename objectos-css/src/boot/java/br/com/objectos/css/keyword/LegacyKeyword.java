package br.com.objectos.css.keyword;

import br.com.objectos.css.type.JustifyItemsValue;

public final class LegacyKeyword extends StandardKeyword implements JustifyItemsValue {
  static final LegacyKeyword INSTANCE = new LegacyKeyword();

  private LegacyKeyword() {
    super(132, "legacy", "legacy");
  }
}
