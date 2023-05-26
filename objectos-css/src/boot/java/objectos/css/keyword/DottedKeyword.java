package objectos.css.keyword;

import objectos.css.type.LineStyleValue;
import objectos.css.type.TextDecorationStyleValue;

public final class DottedKeyword extends StandardKeyword implements LineStyleValue, TextDecorationStyleValue {
  static final DottedKeyword INSTANCE = new DottedKeyword();

  private DottedKeyword() {
    super(69, "dotted", "dotted");
  }
}
