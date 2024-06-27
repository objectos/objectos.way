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

// TODO make package-private when done with the refactoring
public interface HttpModuleMatcher {

  record Exact(String value) implements HttpModuleMatcher {
    @Override
    public final boolean test(Http.Request.Target.Path path) {
      return path.is(value);
    }
  }

  record Matcher2(HttpModuleMatcher matcher1, HttpModuleMatcher matcher2) implements HttpModuleMatcher {
    @Override
    public final HttpModuleMatcher append(HttpModuleMatcher other) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final boolean test(Http.Request.Target.Path path) {
      return matcher1.test(path) && matcher2.test(path);
    }
  }

  record NamedVariable(String name) implements HttpModuleMatcher {
    @Override
    public final boolean test(Http.Request.Target.Path path) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  record StartsWith(String value) implements HttpModuleMatcher {
    @Override
    public final boolean test(Http.Request.Target.Path path) {
      return path.startsWith(value);
    }
  }

  default HttpModuleMatcher append(HttpModuleMatcher other) {
    return new Matcher2(this, other);
  }

  boolean test(Http.Request.Target.Path path);

}