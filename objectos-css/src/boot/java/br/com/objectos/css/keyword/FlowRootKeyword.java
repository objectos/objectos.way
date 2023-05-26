package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInsideValue;

public final class FlowRootKeyword extends StandardKeyword implements DisplayInsideValue {
  static final FlowRootKeyword INSTANCE = new FlowRootKeyword();

  private FlowRootKeyword() {
    super(84, "flowRoot", "flow-root");
  }
}
