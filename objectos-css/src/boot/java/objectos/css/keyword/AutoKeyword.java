package objectos.css.keyword;

import objectos.css.type.AlignSelfValue;
import objectos.css.type.AppearanceValue;
import objectos.css.type.BackgroundSizeArity2Value;
import objectos.css.type.BottomValue;
import objectos.css.type.CursorValue;
import objectos.css.type.FlexArity1Value;
import objectos.css.type.HeightOrWidthValue;
import objectos.css.type.JustifySelfValue;
import objectos.css.type.LeftValue;
import objectos.css.type.MarginWidthValue;
import objectos.css.type.MaxHeightOrWidthValue;
import objectos.css.type.OutlineStyleValue;
import objectos.css.type.OverflowValue;
import objectos.css.type.RightValue;
import objectos.css.type.TextDecorationThicknessValue;
import objectos.css.type.TextSizeAdjustValue;
import objectos.css.type.TopValue;
import objectos.css.type.ZIndexValue;

public final class AutoKeyword extends StandardKeyword implements AlignSelfValue, AppearanceValue, BackgroundSizeArity2Value, BottomValue, CursorValue, FlexArity1Value, HeightOrWidthValue, JustifySelfValue, LeftValue, MarginWidthValue, MaxHeightOrWidthValue, OutlineStyleValue, OverflowValue, RightValue, TextDecorationThicknessValue, TextSizeAdjustValue, TopValue, ZIndexValue {
  static final AutoKeyword INSTANCE = new AutoKeyword();

  private AutoKeyword() {
    super(22, "auto", "auto");
  }
}
