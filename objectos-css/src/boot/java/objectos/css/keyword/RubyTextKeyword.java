package objectos.css.keyword;

import objectos.css.type.DisplayInternalValue;

public final class RubyTextKeyword extends StandardKeyword implements DisplayInternalValue {
  static final RubyTextKeyword INSTANCE = new RubyTextKeyword();

  private RubyTextKeyword() {
    super(202, "rubyText", "ruby-text");
  }
}
