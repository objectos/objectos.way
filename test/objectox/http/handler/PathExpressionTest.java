/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.handler;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PathExpressionTest {

  @Test
  public void build01() {
    final Set<String> paramNames;
    paramNames = Set.of();

    final List<Segment> segments;
    segments = List.of(new SegmentExact("/test"));

    final PathExpression subject;
    subject = new PathExpression(paramNames, segments);

    assertEquals(
        subject.build(),

        segments
    );
  }

  @Test
  public void build02() {
    final Set<String> paramNames;
    paramNames = Set.of("foo");

    final List<Segment> segments;
    segments = List.of(new SegmentRegion("/test"), new SegmentParamLast("foo"));

    final PathExpression subject;
    subject = new PathExpression(paramNames, segments);

    assertEquals(
        subject.build(),

        segments
    );
  }

  @Test
  public void build03() {
    final Set<String> paramNames;
    paramNames = Set.of("foo");

    final List<Segment> segments;
    segments = List.of(new SegmentRegion("/test"), new SegmentParamLast("foo"));

    final PathExpression subject;
    subject = new PathExpression(paramNames, segments);

    subject.pathParamNamed(new PathParamNamed("foo", PathParamPredicates.DIGITS));

    assertEquals(
        subject.build(),

        List.of(
            new SegmentRegion("/test"),

            new SegmentParamLast("foo", PathParamPredicates.DIGITS)
        )
    );
  }

  @Test
  public void build04() {
    final Set<String> paramNames;
    paramNames = Set.of("foo");

    final List<Segment> segments;
    segments = List.of(new SegmentRegion("/"), new SegmentParam("foo", '/'), new SegmentRegion("pdf"));

    final PathExpression subject;
    subject = new PathExpression(paramNames, segments);

    subject.pathParamNamed(new PathParamNamed("foo", PathParamPredicates.DIGITS));

    assertEquals(
        subject.build(),

        List.of(
            new SegmentRegion("/"),

            new SegmentParam("foo", '/', PathParamPredicates.DIGITS),

            new SegmentRegion("pdf")
        )
    );
  }

  @Test
  public void build05() {
    final Set<String> paramNames;
    paramNames = Set.of("foo");

    final List<Segment> segments;
    segments = List.of(new SegmentRegion("/"), new SegmentParam("foo", '/'), new SegmentRegion("pdf"));

    final PathExpression subject;
    subject = new PathExpression(paramNames, segments);

    try {
      subject.pathParamNamed(new PathParamNamed("bar", PathParamPredicates.DIGITS));

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Path expression does not declare a 'bar' path parameter");
    }
  }

}
