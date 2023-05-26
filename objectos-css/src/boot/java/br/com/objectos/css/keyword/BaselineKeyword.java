package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AlignContentValue;
import br.com.objectos.css.type.AlignItemsValue;
import br.com.objectos.css.type.AlignSelfValue;
import br.com.objectos.css.type.JustifyItemsValue;
import br.com.objectos.css.type.JustifySelfValue;
import br.com.objectos.css.type.VerticalAlignValue;

public final class BaselineKeyword extends StandardKeyword implements AlignContentValue, AlignItemsValue, AlignSelfValue, JustifyItemsValue, JustifySelfValue, VerticalAlignValue {
  static final BaselineKeyword INSTANCE = new BaselineKeyword();

  private BaselineKeyword() {
    super(23, "baseline", "baseline");
  }
}
