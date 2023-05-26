package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AlignContentValue;
import br.com.objectos.css.type.AlignItemsValue;
import br.com.objectos.css.type.AlignSelfValue;
import br.com.objectos.css.type.ContentValue;
import br.com.objectos.css.type.FontStyleValue;
import br.com.objectos.css.type.FontVariantCss21Value;
import br.com.objectos.css.type.FontWeightValue;
import br.com.objectos.css.type.JustifyContentValue;
import br.com.objectos.css.type.JustifyItemsValue;
import br.com.objectos.css.type.JustifySelfValue;
import br.com.objectos.css.type.LineHeightValue;
import br.com.objectos.css.type.WhiteSpaceValue;

public final class NormalKeyword extends StandardKeyword implements AlignContentValue, AlignItemsValue, AlignSelfValue, ContentValue, FontStyleValue, FontVariantCss21Value, FontWeightValue, JustifyContentValue, JustifyItemsValue, JustifySelfValue, LineHeightValue, WhiteSpaceValue {
  static final NormalKeyword INSTANCE = new NormalKeyword();

  private NormalKeyword() {
    super(167, "normal", "normal");
  }
}
