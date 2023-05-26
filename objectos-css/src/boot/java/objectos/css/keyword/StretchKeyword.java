package objectos.css.keyword;

import objectos.css.type.AlignItemsValue;
import objectos.css.type.AlignSelfValue;
import objectos.css.type.ContentDistribution;
import objectos.css.type.JustifyItemsValue;
import objectos.css.type.JustifySelfValue;

public final class StretchKeyword extends StandardKeyword implements AlignItemsValue, AlignSelfValue, ContentDistribution, JustifyItemsValue, JustifySelfValue {
  static final StretchKeyword INSTANCE = new StretchKeyword();

  private StretchKeyword() {
    super(235, "stretch", "stretch");
  }
}
