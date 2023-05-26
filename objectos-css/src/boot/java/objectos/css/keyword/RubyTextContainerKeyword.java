package objectos.css.keyword;

import objectos.css.type.DisplayInternalValue;

public final class RubyTextContainerKeyword extends StandardKeyword implements DisplayInternalValue {
  static final RubyTextContainerKeyword INSTANCE = new RubyTextContainerKeyword();

  private RubyTextContainerKeyword() {
    super(203, "rubyTextContainer", "ruby-text-container");
  }
}
