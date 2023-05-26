package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInsideValue;

public final class FlowKeyword extends StandardKeyword implements DisplayInsideValue {
  static final FlowKeyword INSTANCE = new FlowKeyword();

  private FlowKeyword() {
    super(83, "flow", "flow");
  }
}
