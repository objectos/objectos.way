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
public sealed abstract class HttpModuleMatcher permits HttpModuleMatcher.Exact, HttpModuleMatcher.StartsWith, HttpModuleSegments {

  static final class Exact extends HttpModuleMatcher {

    private final String value;

    Exact(String value) {
      this.value = value;
    }

    @Override
    public final boolean equals(Object obj) {
      return obj == this || obj instanceof Exact that
          && value.equals(that.value);
    }

    @Override
    public final int hashCode() {
      return value.hashCode();
    }

    @Override
    final boolean test(Http.Request.Target.Path path) {
      return path.is(value);
    }

  }

  static final class StartsWith extends HttpModuleMatcher {

    private final String value;

    StartsWith(String value) {
      this.value = value;
    }

    @Override
    public final boolean equals(Object obj) {
      return obj == this || obj instanceof StartsWith that
          && value.equals(that.value);
    }

    @Override
    public final int hashCode() {
      return value.hashCode();
    }

    @Override
    final boolean test(Http.Request.Target.Path path) {
      return path.startsWith(value);
    }

  }

  HttpModuleMatcher() {}

  abstract boolean test(Http.Request.Target.Path path);

}