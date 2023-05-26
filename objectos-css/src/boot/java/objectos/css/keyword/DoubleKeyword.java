package objectos.css.keyword;

import objectos.css.type.LineStyleValue;
import objectos.css.type.TextDecorationStyleValue;

public final class DoubleKeyword extends StandardKeyword implements LineStyleValue, TextDecorationStyleValue {
  static final DoubleKeyword INSTANCE = new DoubleKeyword();

  private DoubleKeyword() {
    super(70, "doubleKw", "double");
  }
}
