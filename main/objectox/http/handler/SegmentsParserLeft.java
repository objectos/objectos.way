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

final class SegmentsParserLeft {

  final SegmentsParser ctx;

  SegmentsParserLeft(SegmentsParser ctx) {
    this.ctx = ctx;
  }

  public final void execute() {
    final int startIndex;
    startIndex = ctx.index();

    final int left;
    left = ctx.indexOf('{');

    if (left == -1) {
      final String exact;
      exact = ctx.substring(startIndex);

      final Segment segment;
      segment = new SegmentExact(exact);

      ctx.add(segment);

      ctx.end();
    } else {
      final String region;
      region = ctx.substring(startIndex, left);

      final Segment segment;
      segment = new SegmentRegion(region);

      ctx.add(segment);
    }
  }

}
