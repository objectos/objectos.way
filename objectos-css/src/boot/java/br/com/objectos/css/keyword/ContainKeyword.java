package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BackgroundSizeArity1Value;
import br.com.objectos.css.type.ObjectFitValue;

public final class ContainKeyword extends StandardKeyword implements BackgroundSizeArity1Value, ObjectFitValue {
  static final ContainKeyword INSTANCE = new ContainKeyword();

  private ContainKeyword() {
    super(52, "contain", "contain");
  }
}
