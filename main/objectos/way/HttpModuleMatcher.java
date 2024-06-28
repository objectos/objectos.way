/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

@SuppressWarnings("exports")
interface HttpModuleMatcher {

  record Exact(String value) implements HttpModuleMatcher {
    @Override
    public final boolean test(HttpRequestTargetPath path) {
      return path.exact(value);
    }
  }

  record Matcher2(HttpModuleMatcher matcher1, HttpModuleMatcher matcher2) implements HttpModuleMatcher {
    @Override
    public final HttpModuleMatcher append(HttpModuleMatcher other) {
      return new Matcher3(matcher1, matcher2, other);
    }

    @Override
    public final boolean test(HttpRequestTargetPath path) {
      return matcher1.test(path)
          && matcher2.test(path)
          && path.atEnd();
    }
  }

  record Matcher3(HttpModuleMatcher matcher1, HttpModuleMatcher matcher2, HttpModuleMatcher matcher3) implements HttpModuleMatcher {
    @Override
    public final HttpModuleMatcher append(HttpModuleMatcher other) {
      return new Matcher4(matcher1, matcher2, matcher3, other);
    }

    @Override
    public final boolean test(HttpRequestTargetPath path) {
      return matcher1.test(path)
          && matcher2.test(path)
          && matcher3.test(path)
          && path.atEnd();
    }
  }

  record Matcher4(HttpModuleMatcher matcher1,
                  HttpModuleMatcher matcher2,
                  HttpModuleMatcher matcher3,
                  HttpModuleMatcher matcher4)
      implements HttpModuleMatcher {
    @Override
    public final HttpModuleMatcher append(HttpModuleMatcher other) {
      return new Matcher5(matcher1, matcher2, matcher3, matcher4, other);
    }

    @Override
    public final boolean test(HttpRequestTargetPath path) {
      return matcher1.test(path)
          && matcher2.test(path)
          && matcher3.test(path)
          && matcher4.test(path)
          && path.atEnd();
    }
  }

  record Matcher5(HttpModuleMatcher matcher1,
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
    public final boolean test(HttpRequestTargetPath path) {
      return matcher1.test(path)
          && matcher2.test(path)
          && matcher3.test(path)
          && matcher4.test(path)
          && matcher5.test(path)
          && path.atEnd();
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
    public final boolean test(HttpRequestTargetPath path) {
      for (HttpModuleMatcher matcher : matchers) {
        if (!matcher.test(path)) {
          return false;
        }
      }

      return path.atEnd();
    }
  }

  record NamedVariable(String name) implements HttpModuleMatcher {
    @Override
    public final boolean test(HttpRequestTargetPath path) {
      return path.namedVariable(name);
    }
  }

  record Region(String value) implements HttpModuleMatcher {
    @Override
    public final boolean test(HttpRequestTargetPath path) {
      return path.region(value);
    }
  }

  record StartsWith(String value) implements HttpModuleMatcher {
    @Override
    public final boolean test(HttpRequestTargetPath path) {
      return path.startsWithMatcher(value);
    }
  }

  default HttpModuleMatcher append(HttpModuleMatcher other) {
    return new Matcher2(this, other);
  }

  boolean test(HttpRequestTargetPath path);

}