package objectos.css.keyword;

import objectos.css.type.FontFamilyValue;

public final class MonospaceKeyword extends StandardKeyword implements FontFamilyValue {
  static final MonospaceKeyword INSTANCE = new MonospaceKeyword();

  private MonospaceKeyword() {
    super(156, "monospace", "monospace");
  }
}
