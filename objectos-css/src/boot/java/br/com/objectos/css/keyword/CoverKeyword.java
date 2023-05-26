package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BackgroundSizeArity1Value;
import br.com.objectos.css.type.ObjectFitValue;

public final class CoverKeyword extends StandardKeyword implements BackgroundSizeArity1Value, ObjectFitValue {
  static final CoverKeyword INSTANCE = new CoverKeyword();

  private CoverKeyword() {
    super(58, "cover", "cover");
  }
}
