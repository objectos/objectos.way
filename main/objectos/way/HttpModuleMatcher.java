/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

import java.util.Arrays;
import java.util.function.Predicate;
import objectos.way.HttpModule.Condition;

@SuppressWarnings("exports")
interface HttpModuleMatcher extends Predicate<Http.Request> {

  record Exact(String value) implements HttpModuleMatcher {
    @Override
    public final boolean test(HttpModuleSupport target) {
      return target.exact(value);
    }
  }

  record Matcher2(
      HttpModuleMatcher matcher1,
      HttpModuleMatcher matcher2)
      implements HttpModuleMatcher {
    @Override
    public final HttpModuleMatcher append(HttpModuleMatcher other) {
      return new Matcher3(matcher1, matcher2, other);
    }

    @Override
    public final boolean test(HttpModuleSupport target) {
      return matcher1.test(target)
          && matcher2.test(target)
          && target.atEnd();
    }
  }

  record Matcher3(
      HttpModuleMatcher matcher1,
      HttpModuleMatcher matcher2,
      HttpModuleMatcher matcher3)
      implements HttpModuleMatcher {
    @Override
    public final HttpModuleMatcher append(HttpModuleMatcher other) {
      return new Matcher4(matcher1, matcher2, matcher3, other);
    }

    @Override
    public final boolean test(HttpModuleSupport target) {
      return matcher1.test(target)
          && matcher2.test(target)
          && matcher3.test(target)
          && target.atEnd();
    }
  }

  record Matcher4(
      HttpModuleMatcher matcher1,
      HttpModuleMatcher matcher2,
      HttpModuleMatcher matcher3,
      HttpModuleMatcher matcher4)
      implements HttpModuleMatcher {
    @Override
    public final HttpModuleMatcher append(HttpModuleMatcher other) {
      return new Matcher5(matcher1, matcher2, matcher3, matcher4, other);
    }

    @Override
    public final boolean test(HttpModuleSupport target) {
      return matcher1.test(target)
          && matcher2.test(target)
          && matcher3.test(target)
          && matcher4.test(target)
          && target.atEnd();
    }
  }

  record Matcher5(
      HttpModuleMatcher matcher1,
      HttpModuleMatcher matcher2,
      HttpModuleMatcher matcher3,
      HttpModuleMatcher matcher4,
      HttpModuleMatcher matcher5)
      implements HttpModuleMatcher {
    @Override
    public final HttpModuleMatcher append(HttpModuleMatcher other) {
      return new MatcherN(matcher1, matcher2, matcher3, matcher4, matcher5, other);
    }

    @Override
    public final boolean test(HttpModuleSupport target) {
      return matcher1.test(target)
          && matcher2.test(target)
          && matcher3.test(target)
          && matcher4.test(target)
          && matcher5.test(target)
          && target.atEnd();
    }
  }

  record MatcherN(HttpModuleMatcher... matchers)
      implements HttpModuleMatcher {
    @Override
    public final HttpModuleMatcher append(HttpModuleMatcher other) {
      HttpModuleMatcher[] copy;
      copy = Arrays.copyOf(matchers, matchers.length + 1);

      copy[matchers.length] = other;

      return new MatcherN(copy);
    }

    @Override
    public final boolean test(HttpModuleSupport target) {
      for (HttpModuleMatcher matcher : matchers) {
        if (!matcher.test(target)) {
          return false;
        }
      }

      return target.atEnd();
    }
  }

  record NamedVariable(String name) implements HttpModuleMatcher {
    @Override
    public final boolean test(HttpModuleSupport target) {
      return target.namedVariable(name);
    }
  }

  record Region(String value) implements HttpModuleMatcher {
    @Override
    public final boolean test(HttpModuleSupport target) {
      return target.region(value);
    }
  }

  record StartsWith(String value) implements HttpModuleMatcher {
    @Override
    public final boolean test(HttpModuleSupport target) {
      return target.startsWithMatcher(value);
    }
  }

  record WithCondition(HttpModuleMatcher matcher, HttpModuleCondition condition) implements HttpModuleMatcher {
    @Override
    public final boolean test(HttpModuleSupport path) {
      return matcher.test(path) && condition.test(path);
    }
  }

  record WithConditions(HttpModuleMatcher matcher, HttpModule.Condition[] conditions) implements HttpModuleMatcher {
    @Override
    public final boolean test(HttpModuleSupport path) {
      return matcher.test(path) && testConditions(path);
    }

    private boolean testConditions(HttpModuleSupport path) {
      for (HttpModule.Condition condition : conditions) {
        if (!condition.test(path)) {
          return false;
        }
      }

      return true;
    }
  }

  default HttpModuleMatcher append(HttpModuleMatcher other) {
    return new Matcher2(this, other);
  }

  default HttpModuleMatcher withCondition(HttpModuleCondition condition) {
    return new WithCondition(this, condition);
  }

  default HttpModuleMatcher withConditions(Condition[] conditions) {
    return new WithConditions(this, conditions);
  }

  @Override
  default boolean test(Http.Request request) {
    final HttpModuleSupport target;
    target = (HttpModuleSupport) request;

    target.matcherReset();

    return test(target);
  }

  boolean test(HttpModuleSupport target);

}