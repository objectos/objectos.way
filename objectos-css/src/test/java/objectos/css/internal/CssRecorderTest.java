/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import objectos.css.tmpl.Instruction;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CssRecorderTest {

  private final CssRecorder recorder = new CssRecorder();

  @Test(description = """
  CssRecorder

  - style(BODY);
  """)
  public void testCase00() {
    executeBefore();

    addRule(ExternalTypeSelector.BODY);

    executeAfter();

    testProto(
      // [0]: BODY
      ByteProto.MARKED,
      ByteProto.NULL,
      ExternalTypeSelector.BODY.ordinal(),
      0,
      ByteProto.TYPE_SELECTOR,

      // [5]: style()
      ByteProto.RULE,
      13,
      ByteProto.TYPE_SELECTOR,
      0,
      ByteProto.RULE_END,
      0, // contents
      5, // start
      ByteProto.RULE,

      // [13]: ROOT
      ByteProto.ROOT,
      ByteProto.RULE,
      5,
      ByteProto.ROOT_END,
      13
    );
  }

  private void addRule(Instruction... elements) {
    recorder.addRule(elements);
  }

  private void executeAfter() {
    recorder.executeAfter();
  }

  private void executeBefore() {
    recorder.executeBefore();
  }

  private void testProto(int... expected) {
    int[] protos = Arrays.copyOf(recorder.protoArray, recorder.protoIndex);

    if (protos.length != expected.length) {
      Assert.fail("""
      protos length differ

      actual  : %s
      expected: %s
      """.formatted(Arrays.toString(protos), Arrays.toString(expected)));
    }

    assertEquals(protos, expected);
  }

}