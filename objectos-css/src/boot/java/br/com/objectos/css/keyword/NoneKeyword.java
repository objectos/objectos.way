package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AppearanceValue;
import br.com.objectos.css.type.BackgroundImageValue;
import br.com.objectos.css.type.ClearValue;
import br.com.objectos.css.type.ContentValue;
import br.com.objectos.css.type.CursorValue;
import br.com.objectos.css.type.DisplayBoxValue;
import br.com.objectos.css.type.FlexArity1Value;
import br.com.objectos.css.type.FloatValue;
import br.com.objectos.css.type.LineStyleValue;
import br.com.objectos.css.type.ListStyleImageValue;
import br.com.objectos.css.type.ListStyleTypeValue;
import br.com.objectos.css.type.MaxHeightOrWidthValue;
import br.com.objectos.css.type.ObjectFitValue;
import br.com.objectos.css.type.ResizeValue;
import br.com.objectos.css.type.TextDecorationLineValue;
import br.com.objectos.css.type.TextSizeAdjustValue;
import br.com.objectos.css.type.TextTransformValue;
import br.com.objectos.css.type.TransformValue;

public final class NoneKeyword extends StandardKeyword implements AppearanceValue, BackgroundImageValue, ClearValue, ContentValue, CursorValue, DisplayBoxValue, FlexArity1Value, FloatValue, LineStyleValue, ListStyleImageValue, ListStyleTypeValue, MaxHeightOrWidthValue, ObjectFitValue, ResizeValue, TextDecorationLineValue, TextSizeAdjustValue, TextTransformValue, TransformValue {
  static final NoneKeyword INSTANCE = new NoneKeyword();

  private NoneKeyword() {
    super(166, "none", "none");
  }
}
