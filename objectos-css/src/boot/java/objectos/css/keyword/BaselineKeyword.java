package objectos.css.keyword;

import objectos.css.type.AlignContentValue;
import objectos.css.type.AlignItemsValue;
import objectos.css.type.AlignSelfValue;
import objectos.css.type.JustifyItemsValue;
import objectos.css.type.JustifySelfValue;
import objectos.css.type.VerticalAlignValue;

public final class BaselineKeyword extends StandardKeyword implements AlignContentValue, AlignItemsValue, AlignSelfValue, JustifyItemsValue, JustifySelfValue, VerticalAlignValue {
  static final BaselineKeyword INSTANCE = new BaselineKeyword();

  private BaselineKeyword() {
    super(23, "baseline", "baseline");
  }
}
