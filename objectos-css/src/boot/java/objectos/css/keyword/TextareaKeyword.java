package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class TextareaKeyword extends StandardKeyword implements AppearanceValue {
  static final TextareaKeyword INSTANCE = new TextareaKeyword();

  private TextareaKeyword() {
    super(253, "textareaKw", "textarea");
  }
}
