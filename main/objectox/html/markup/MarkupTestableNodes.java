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
package objectox.html.markup;

import objectos.html.TestableNodes;
import objectos.html.markup.TestableMarkup;

public final class MarkupTestableNodes implements TestableNodes {

  private final TestableRecorder recorder;

  MarkupTestableNodes(TestableRecorder recorder) {
    this.recorder = recorder;
  }

  @Override
  public final TestableMarkup cell(int width, String value) {
    return recorder.cell(width, value);
  }

  @Override
  public final TestableMarkup h1(String value) {
    return recorder.heading(1, value);
  }

  @Override
  public final TestableMarkup h2(String value) {
    return recorder.heading(2, value);
  }

  @Override
  public final TestableMarkup h3(String value) {
    return recorder.heading(3, value);
  }

  @Override
  public final TestableMarkup h4(String value) {
    return recorder.heading(4, value);
  }

  @Override
  public final TestableMarkup h5(String value) {
    return recorder.heading(5, value);
  }

  @Override
  public final TestableMarkup h6(String value) {
    return recorder.heading(6, value);
  }

}
