package objectos.css.keyword;

import objectos.css.type.FontStyleValue;

public final class ItalicKeyword extends StandardKeyword implements FontStyleValue {
  static final ItalicKeyword INSTANCE = new ItalicKeyword();

  private ItalicKeyword() {
    super(116, "italic", "italic");
  }
}
