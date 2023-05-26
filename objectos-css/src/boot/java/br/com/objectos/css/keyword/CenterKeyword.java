package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BackgroundPositionValue;
import br.com.objectos.css.type.ContentPosition;
import br.com.objectos.css.type.JustifyLegacyValue;
import br.com.objectos.css.type.SelfPosition;
import br.com.objectos.css.type.TextAlignValue;

public final class CenterKeyword extends StandardKeyword implements BackgroundPositionValue, ContentPosition, JustifyLegacyValue, SelfPosition, TextAlignValue {
  static final CenterKeyword INSTANCE = new CenterKeyword();

  private CenterKeyword() {
    super(39, "center", "center");
  }
}
