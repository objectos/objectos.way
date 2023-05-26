package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AppearanceValue;

public final class PushButtonKeyword extends StandardKeyword implements AppearanceValue {
  static final PushButtonKeyword INSTANCE = new PushButtonKeyword();

  private PushButtonKeyword() {
    super(187, "pushButton", "push-button");
  }
}
