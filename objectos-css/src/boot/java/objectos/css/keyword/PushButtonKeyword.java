package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class PushButtonKeyword extends StandardKeyword implements AppearanceValue {
  static final PushButtonKeyword INSTANCE = new PushButtonKeyword();

  private PushButtonKeyword() {
    super(187, "pushButton", "push-button");
  }
}
