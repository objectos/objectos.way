package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AlignSelfValue;
import br.com.objectos.css.type.AppearanceValue;
import br.com.objectos.css.type.BackgroundSizeArity2Value;
import br.com.objectos.css.type.BottomValue;
import br.com.objectos.css.type.CursorValue;
import br.com.objectos.css.type.FlexArity1Value;
import br.com.objectos.css.type.HeightOrWidthValue;
import br.com.objectos.css.type.JustifySelfValue;
import br.com.objectos.css.type.LeftValue;
import br.com.objectos.css.type.MarginWidthValue;
import br.com.objectos.css.type.MaxHeightOrWidthValue;
import br.com.objectos.css.type.OutlineStyleValue;
import br.com.objectos.css.type.OverflowValue;
import br.com.objectos.css.type.RightValue;
import br.com.objectos.css.type.TextDecorationThicknessValue;
import br.com.objectos.css.type.TextSizeAdjustValue;
import br.com.objectos.css.type.TopValue;
import br.com.objectos.css.type.ZIndexValue;

public final class AutoKeyword extends StandardKeyword implements AlignSelfValue, AppearanceValue, BackgroundSizeArity2Value, BottomValue, CursorValue, FlexArity1Value, HeightOrWidthValue, JustifySelfValue, LeftValue, MarginWidthValue, MaxHeightOrWidthValue, OutlineStyleValue, OverflowValue, RightValue, TextDecorationThicknessValue, TextSizeAdjustValue, TopValue, ZIndexValue {
  static final AutoKeyword INSTANCE = new AutoKeyword();

  private AutoKeyword() {
    super(22, "auto", "auto");
  }
}
