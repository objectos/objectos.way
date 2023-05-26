package objectos.css.keyword;

import objectos.css.type.LineWidthValue;
import objectos.css.type.OutlineWidthValue;

public final class ThickKeyword extends StandardKeyword implements LineWidthValue, OutlineWidthValue {
  static final ThickKeyword INSTANCE = new ThickKeyword();

  private ThickKeyword() {
    super(256, "thick", "thick");
  }
}
