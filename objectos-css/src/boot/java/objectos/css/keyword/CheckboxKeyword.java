package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class CheckboxKeyword extends StandardKeyword implements AppearanceValue {
  static final CheckboxKeyword INSTANCE = new CheckboxKeyword();

  private CheckboxKeyword() {
    super(40, "checkbox", "checkbox");
  }
}
