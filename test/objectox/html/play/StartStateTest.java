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
package objectox.html.play;

import static org.testng.Assert.assertEquals;

import objectox.html.HtmlByteProto;
import org.testng.annotations.Test;

public class StartStateTest {

  @Test(description = "empty")
  public void compute00() {
    final Tape tape;
    tape = TapeY.create(opts -> {
      opts.main();
    });

    final StartState subject;
    subject = new StartState(tape);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), BeginDocumentState.class);
  }

  @Test(description = "root element")
  public void compute01() {
    final Tape tape;
    tape = TapeY.create(opts -> {
      opts.main(
          HtmlByteProto.ELEMENT
      );
    });

    final StartState subject;
    subject = new StartState(tape);

    final State res;
    res = subject.compute();

    assertEquals(res.getClass(), BeginDocumentState.class);
  }

}
