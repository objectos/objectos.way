package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AlignItemsValue;
import br.com.objectos.css.type.AlignSelfValue;
import br.com.objectos.css.type.ContentDistribution;
import br.com.objectos.css.type.JustifyItemsValue;
import br.com.objectos.css.type.JustifySelfValue;

public final class StretchKeyword extends StandardKeyword implements AlignItemsValue, AlignSelfValue, ContentDistribution, JustifyItemsValue, JustifySelfValue {
  static final StretchKeyword INSTANCE = new StretchKeyword();

  private StretchKeyword() {
    super(235, "stretch", "stretch");
  }
}
