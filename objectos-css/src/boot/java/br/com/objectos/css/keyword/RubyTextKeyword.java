package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInternalValue;

public final class RubyTextKeyword extends StandardKeyword implements DisplayInternalValue {
  static final RubyTextKeyword INSTANCE = new RubyTextKeyword();

  private RubyTextKeyword() {
    super(202, "rubyText", "ruby-text");
  }
}
