package objectos.css.keyword;

import objectos.css.type.DisplayInternalValue;

public final class RubyBaseContainerKeyword extends StandardKeyword implements DisplayInternalValue {
  static final RubyBaseContainerKeyword INSTANCE = new RubyBaseContainerKeyword();

  private RubyBaseContainerKeyword() {
    super(201, "rubyBaseContainer", "ruby-base-container");
  }
}
