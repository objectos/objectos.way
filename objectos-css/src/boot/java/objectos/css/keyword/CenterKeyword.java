package objectos.css.keyword;

import objectos.css.type.BackgroundPositionValue;
import objectos.css.type.ContentPosition;
import objectos.css.type.JustifyLegacyValue;
import objectos.css.type.SelfPosition;
import objectos.css.type.TextAlignValue;

public final class CenterKeyword extends StandardKeyword implements BackgroundPositionValue, ContentPosition, JustifyLegacyValue, SelfPosition, TextAlignValue {
  static final CenterKeyword INSTANCE = new CenterKeyword();

  private CenterKeyword() {
    super(39, "center", "center");
  }
}
