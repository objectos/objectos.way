package objectos.css.keyword;

import objectos.css.type.AlignContentValue;
import objectos.css.type.AlignItemsValue;
import objectos.css.type.AlignSelfValue;
import objectos.css.type.ContentValue;
import objectos.css.type.FontStyleValue;
import objectos.css.type.FontVariantCss21Value;
import objectos.css.type.FontWeightValue;
import objectos.css.type.JustifyContentValue;
import objectos.css.type.JustifyItemsValue;
import objectos.css.type.JustifySelfValue;
import objectos.css.type.LineHeightValue;
import objectos.css.type.WhiteSpaceValue;

public final class NormalKeyword extends StandardKeyword implements AlignContentValue, AlignItemsValue, AlignSelfValue, ContentValue, FontStyleValue, FontVariantCss21Value, FontWeightValue, JustifyContentValue, JustifyItemsValue, JustifySelfValue, LineHeightValue, WhiteSpaceValue {
  static final NormalKeyword INSTANCE = new NormalKeyword();

  private NormalKeyword() {
    super(167, "normal", "normal");
  }
}
