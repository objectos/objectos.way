package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ObjectFitValue;

public final class ScaleDownKeyword extends StandardKeyword implements ObjectFitValue {
  static final ScaleDownKeyword INSTANCE = new ScaleDownKeyword();

  private ScaleDownKeyword() {
    super(208, "scaleDown", "scale-down");
  }
}
