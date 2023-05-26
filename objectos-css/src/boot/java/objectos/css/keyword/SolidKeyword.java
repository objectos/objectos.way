package objectos.css.keyword;

import objectos.css.type.LineStyleValue;
import objectos.css.type.TextDecorationStyleValue;

public final class SolidKeyword extends StandardKeyword implements LineStyleValue, TextDecorationStyleValue {
  static final SolidKeyword INSTANCE = new SolidKeyword();

  private SolidKeyword() {
    super(223, "solid", "solid");
  }
}
