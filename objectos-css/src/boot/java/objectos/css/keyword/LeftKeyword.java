package objectos.css.keyword;

import objectos.css.type.BackgroundPositionValue;
import objectos.css.type.ClearValue;
import objectos.css.type.ContentPositionOrLeftOrRight;
import objectos.css.type.FloatValue;
import objectos.css.type.JustifyLegacyValue;
import objectos.css.type.SelfPositionOrLeftOrRight;
import objectos.css.type.TextAlignValue;

public final class LeftKeyword extends StandardKeyword implements BackgroundPositionValue, ClearValue, ContentPositionOrLeftOrRight, FloatValue, JustifyLegacyValue, SelfPositionOrLeftOrRight, TextAlignValue {
  static final LeftKeyword INSTANCE = new LeftKeyword();

  private LeftKeyword() {
    super(131, "left", "left");
  }
}
