package objectos.css.keyword;

import objectos.css.type.LineStyleValue;

public final class InsetKeyword extends StandardKeyword implements LineStyleValue {
  static final InsetKeyword INSTANCE = new InsetKeyword();

  private InsetKeyword() {
    super(113, "inset", "inset");
  }
}