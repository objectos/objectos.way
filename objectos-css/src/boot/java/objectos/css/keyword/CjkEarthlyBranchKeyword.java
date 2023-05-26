package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class CjkEarthlyBranchKeyword extends StandardKeyword implements CounterStyleValue {
  static final CjkEarthlyBranchKeyword INSTANCE = new CjkEarthlyBranchKeyword();

  private CjkEarthlyBranchKeyword() {
    super(43, "cjkEarthlyBranch", "cjk-earthly-branch");
  }
}
