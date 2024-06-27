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

import java.util.List;
import objectos.way.HttpModule.Condition;

sealed abstract class HttpModuleSegments implements HttpModuleMatcher {

  private static final class Segments1 extends HttpModuleSegments {

    Segments1(Condition condition) {
      super(condition);
    }

    @Override
    final int count() { return 1; }

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments) {
      return last.test(segments, 0);
    }

  }

  private static final class Segments2 extends HttpModuleSegments {

    private final Condition condition0;

    Segments2(Condition condition0, Condition condition1) {
      super(condition1);

      this.condition0 = condition0;
    }

    @Override
    final int count() { return 2; }

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments) {
      if (!condition0.test(segments, 0)) {
        return false;
      }

      return last.test(segments, 1);
    }

  }

  private static final class Segments3 extends HttpModuleSegments {

    private final Condition condition0;

    private final Condition condition1;

    Segments3(Condition condition0, Condition condition1, Condition condition2) {
      super(condition2);

      this.condition0 = condition0;
      this.condition1 = condition1;
    }

    @Override
    final int count() { return 3; }

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments) {
      if (!condition0.test(segments, 0)) {
        return false;
      }

      if (!condition1.test(segments, 1)) {
        return false;
      }

      return last.test(segments, 2);
    }

  }

  final Condition last;

  HttpModuleSegments(Condition last) {
    this.last = last;
  }

  public static HttpModuleMatcher of(Condition condition) {
    return new Segments1(condition);
  }

  public static HttpModuleMatcher of(Condition c0, Condition c1) {
    return new Segments2(c0, c1);
  }

  public static HttpModuleMatcher of(Condition c0, Condition c1, Condition c2) {
    return new Segments3(c0, c1, c2);
  }

  @Override
  public final boolean test(Http.Request.Target.Path path) {
    List<Http.Request.Target.Path.Segment> segments;
    segments = path.segments();

    if (!last.mustBeLast() && segments.size() != count()) {
      return false;
    }

    return test(segments);
  }

  abstract int count();

  abstract boolean test(List<Http.Request.Target.Path.Segment> segments);

}