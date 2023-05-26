package br.com.objectos.css.keyword;

import br.com.objectos.css.type.HeightOrWidthValue;
import br.com.objectos.css.type.MaxHeightOrWidthValue;

public final class MaxContentKeyword extends StandardKeyword implements HeightOrWidthValue, MaxHeightOrWidthValue {
  static final MaxContentKeyword INSTANCE = new MaxContentKeyword();

  private MaxContentKeyword() {
    super(146, "maxContent", "max-content");
  }
}
