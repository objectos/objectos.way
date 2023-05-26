package objectos.css.keyword;

import objectos.css.type.AppearanceValue;
import objectos.css.type.BackgroundImageValue;
import objectos.css.type.ClearValue;
import objectos.css.type.ContentValue;
import objectos.css.type.CursorValue;
import objectos.css.type.DisplayBoxValue;
import objectos.css.type.FlexArity1Value;
import objectos.css.type.FloatValue;
import objectos.css.type.LineStyleValue;
import objectos.css.type.ListStyleImageValue;
import objectos.css.type.ListStyleTypeValue;
import objectos.css.type.MaxHeightOrWidthValue;
import objectos.css.type.ObjectFitValue;
import objectos.css.type.ResizeValue;
import objectos.css.type.TextDecorationLineValue;
import objectos.css.type.TextSizeAdjustValue;
import objectos.css.type.TextTransformValue;
import objectos.css.type.TransformValue;

public final class NoneKeyword extends StandardKeyword implements AppearanceValue, BackgroundImageValue, ClearValue, ContentValue, CursorValue, DisplayBoxValue, FlexArity1Value, FloatValue, LineStyleValue, ListStyleImageValue, ListStyleTypeValue, MaxHeightOrWidthValue, ObjectFitValue, ResizeValue, TextDecorationLineValue, TextSizeAdjustValue, TextTransformValue, TransformValue {
  static final NoneKeyword INSTANCE = new NoneKeyword();

  private NoneKeyword() {
    super(166, "none", "none");
  }
}
