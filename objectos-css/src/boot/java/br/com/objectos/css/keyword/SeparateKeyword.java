package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BorderCollapseValue;

public final class SeparateKeyword extends StandardKeyword implements BorderCollapseValue {
  static final SeparateKeyword INSTANCE = new SeparateKeyword();

  private SeparateKeyword() {
    super(214, "separate", "separate");
  }
}
