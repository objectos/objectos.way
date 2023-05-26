package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class MozCjkEarthlyBranchKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozCjkEarthlyBranchKeyword INSTANCE = new MozCjkEarthlyBranchKeyword();

  private MozCjkEarthlyBranchKeyword() {
    super(2, "mozCjkEarthlyBranch", "-moz-cjk-earthly-branch");
  }
}
