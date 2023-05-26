package objectos.css.keyword;

import objectos.css.type.FontSizeValue;
import objectos.css.type.LineWidthValue;
import objectos.css.type.OutlineWidthValue;

public final class MediumKeyword extends StandardKeyword implements FontSizeValue, LineWidthValue, OutlineWidthValue {
  static final MediumKeyword INSTANCE = new MediumKeyword();

  private MediumKeyword() {
    super(147, "medium", "medium");
  }
}
