/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.asciidoc;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class AsciidoctorTest2 {

  @Factory
  public Object[] _factory() {
    var tester = Tester.doctor();

    return new Object[] {
        new ConstrainedBoldTest(tester),
        new ConstrainedItalicTest(tester),
        new ConstrainedMonospaceTest(tester),
        new DocumentTest(tester),
        new InlineMacroTest(tester),
        new LexerTest(tester),
        new ListingBlockTest(tester),
        new ParagraphTest(tester),
        new PreambleTest(tester),
        new SectionTest(tester),
        new SourceCodeBlockTest(tester),
        new UnorderedListTest(tester),
        new UrlMacroTest(tester)
    };
  }

  @Test(enabled = false)
  public void _enableCodeMinings() {
  }

}