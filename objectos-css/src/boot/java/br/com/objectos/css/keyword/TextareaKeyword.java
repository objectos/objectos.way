package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AppearanceValue;

public final class TextareaKeyword extends StandardKeyword implements AppearanceValue {
  static final TextareaKeyword INSTANCE = new TextareaKeyword();

  private TextareaKeyword() {
    super(253, "textareaKw", "textarea");
  }
}
