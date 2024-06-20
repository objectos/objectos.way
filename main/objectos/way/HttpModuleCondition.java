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

sealed abstract class HttpModuleCondition extends HttpModule.Condition {

  private static final class EqualTo extends HttpModuleCondition {

    private final String value;

    EqualTo(String value) {
      this.value = value;
    }

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      if (!hasIndex(segments, index)) {
        return false;
      }

      Http.Request.Target.Path.Segment segment;
      segment = segments.get(index);

      return segment.is(value);
    }

  }

  private static final class NonEmpty extends HttpModuleCondition {

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      if (!hasIndex(segments, index)) {
        return false;
      }

      Http.Request.Target.Path.Segment segment;
      segment = segments.get(index);

      String value;
      value = segment.value();

      return !value.isEmpty();
    }

  }

  private static final class ZeroOrMore extends HttpModuleCondition {

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      return segments.size() >= index;
    }

    @Override
    final boolean mustBeLast() { return true; }

  }

  private static final class Present extends HttpModuleCondition {

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      return hasIndex(segments, index);
    }

  }

  private static final class OneOrMore extends HttpModuleCondition {

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      return segments.size() > index;
    }

    @Override
    final boolean mustBeLast() { return true; }

  }

  HttpModuleCondition() {}

  public static Condition equalTo(String value) {
    return new EqualTo(value);
  }

  public static Condition nonEmpty() {
    return new NonEmpty();
  }

  public static Condition zeroOrMore() {
    return new ZeroOrMore();
  }

  public static Condition present() {
    return new Present();
  }

  public static Condition oneOrMore() {
    return new OneOrMore();
  }

}