package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class DisclosureClosedKeyword extends StandardKeyword implements CounterStyleValue {
  static final DisclosureClosedKeyword INSTANCE = new DisclosureClosedKeyword();

  private DisclosureClosedKeyword() {
    super(67, "disclosureClosed", "disclosure-closed");
  }
}
