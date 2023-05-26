package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontSizeValue;
import br.com.objectos.css.type.LineWidthValue;
import br.com.objectos.css.type.OutlineWidthValue;

public final class MediumKeyword extends StandardKeyword implements FontSizeValue, LineWidthValue, OutlineWidthValue {
  static final MediumKeyword INSTANCE = new MediumKeyword();

  private MediumKeyword() {
    super(147, "medium", "medium");
  }
}
