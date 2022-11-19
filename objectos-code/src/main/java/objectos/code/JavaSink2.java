/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package objectos.code;

import java.io.IOException;
import objectos.lang.Check;

abstract class JavaSink2 extends InternalInterpreter2 {

  public sealed abstract static class Option {

    private static final class SkipExisting extends Option {
      @Override
      final void acceptOfDirectory(JavaSinkOfDirectory sink) {
        sink.skipExising = true;
      }
    }

    static final Option SKIP_EXISTING = new SkipExisting();

    abstract void acceptOfDirectory(JavaSinkOfDirectory sink);

  }

  /**
   * Sole constructor
   */
  protected JavaSink2() {}

  public static JavaSink2 ofStringBuilder(StringBuilder output) {
    Check.notNull(output, "output == null");

    return new JavaSinkOfStringBuilder2(output);
  }

  public static Option skipExisting() {
    return Option.SKIP_EXISTING;
  }

  public void eval(JavaTemplate template) {
    Check.notNull(template, "template == null");

    pass0(template);

    pass1();

    pass2();
  }

  public void write(JavaTemplate template) throws IOException {
    eval(template);
  }

}