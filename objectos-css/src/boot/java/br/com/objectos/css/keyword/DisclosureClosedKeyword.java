package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class DisclosureClosedKeyword extends StandardKeyword implements CounterStyleValue {
  static final DisclosureClosedKeyword INSTANCE = new DisclosureClosedKeyword();

  private DisclosureClosedKeyword() {
    super(67, "disclosureClosed", "disclosure-closed");
  }
}
