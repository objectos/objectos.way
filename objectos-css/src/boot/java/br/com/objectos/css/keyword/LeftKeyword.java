package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BackgroundPositionValue;
import br.com.objectos.css.type.ClearValue;
import br.com.objectos.css.type.ContentPositionOrLeftOrRight;
import br.com.objectos.css.type.FloatValue;
import br.com.objectos.css.type.JustifyLegacyValue;
import br.com.objectos.css.type.SelfPositionOrLeftOrRight;
import br.com.objectos.css.type.TextAlignValue;

public final class LeftKeyword extends StandardKeyword implements BackgroundPositionValue, ClearValue, ContentPositionOrLeftOrRight, FloatValue, JustifyLegacyValue, SelfPositionOrLeftOrRight, TextAlignValue {
  static final LeftKeyword INSTANCE = new LeftKeyword();

  private LeftKeyword() {
    super(131, "left", "left");
  }
}
