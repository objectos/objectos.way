package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class RadioKeyword extends StandardKeyword implements AppearanceValue {
  static final RadioKeyword INSTANCE = new RadioKeyword();

  private RadioKeyword() {
    super(188, "radio", "radio");
  }
}
