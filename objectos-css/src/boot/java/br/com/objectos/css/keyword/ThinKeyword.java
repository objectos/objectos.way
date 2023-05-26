package br.com.objectos.css.keyword;

import br.com.objectos.css.type.LineWidthValue;
import br.com.objectos.css.type.OutlineWidthValue;

public final class ThinKeyword extends StandardKeyword implements LineWidthValue, OutlineWidthValue {
  static final ThinKeyword INSTANCE = new ThinKeyword();

  private ThinKeyword() {
    super(257, "thin", "thin");
  }
}
