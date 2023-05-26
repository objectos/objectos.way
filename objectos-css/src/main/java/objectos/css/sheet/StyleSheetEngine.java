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
package objectos.css.sheet;

import objectos.css.sheet.StyleSheet.Processor;
import objectos.lang.Check;

public final class StyleSheetEngine extends PassSource {

  private final Pass0 pass0 = new Pass0();

  private final Pass1 pass1 = new Pass1();

  private final Pass2 pass2 = new Pass2();

  public final void process(StyleSheet sheet, Processor processor) {
    Check.notNull(sheet, "sheet == null");
    Check.notNull(processor, "processor == null");

    try (pass0; pass1; pass2) {
      sheet.eval(pass0);

      pass0.evalDone();

      pass1.execute(this);

      pass2.execute(this, processor);
    }
  }

  @Override
  final int codeAt(int index) { return pass1.codeAt(index); }

  @Override
  final double doubleAt(int index) { return pass0.doubleAt(index); }

  @Override
  final boolean hasProto() { return pass0.hasProto(); }

  @Override
  final int nextProto() { return pass0.nextProto(); }

  @Override
  final String stringAt(int index) { return pass0.stringAt(index); }

}
