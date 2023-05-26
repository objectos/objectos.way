package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AppearanceValue;

public final class CheckboxKeyword extends StandardKeyword implements AppearanceValue {
  static final CheckboxKeyword INSTANCE = new CheckboxKeyword();

  private CheckboxKeyword() {
    super(40, "checkbox", "checkbox");
  }
}
