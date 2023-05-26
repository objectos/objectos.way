package objectos.css.keyword;

import objectos.css.type.LineStyleValue;
import objectos.css.type.TextDecorationStyleValue;

public final class DashedKeyword extends StandardKeyword implements LineStyleValue, TextDecorationStyleValue {
  static final DashedKeyword INSTANCE = new DashedKeyword();

  private DashedKeyword() {
    super(61, "dashed", "dashed");
  }
}
