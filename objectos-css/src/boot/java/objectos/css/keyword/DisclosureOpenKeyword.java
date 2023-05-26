package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class DisclosureOpenKeyword extends StandardKeyword implements CounterStyleValue {
  static final DisclosureOpenKeyword INSTANCE = new DisclosureOpenKeyword();

  private DisclosureOpenKeyword() {
    super(68, "disclosureOpen", "disclosure-open");
  }
}
