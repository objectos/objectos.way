package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class TextfieldKeyword extends StandardKeyword implements AppearanceValue {
  static final TextfieldKeyword INSTANCE = new TextfieldKeyword();

  private TextfieldKeyword() {
    super(254, "textfield", "textfield");
  }
}
