package objectos.css.keyword;

import objectos.css.type.DisplayInternalValue;

public final class RubyBaseKeyword extends StandardKeyword implements DisplayInternalValue {
  static final RubyBaseKeyword INSTANCE = new RubyBaseKeyword();

  private RubyBaseKeyword() {
    super(200, "rubyBase", "ruby-base");
  }
}
