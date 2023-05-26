package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BoxSizingValue;
import br.com.objectos.css.type.BoxValue;

public final class BorderBoxKeyword extends StandardKeyword implements BoxSizingValue, BoxValue {
  static final BorderBoxKeyword INSTANCE = new BorderBoxKeyword();

  private BorderBoxKeyword() {
    super(29, "borderBox", "border-box");
  }
}
