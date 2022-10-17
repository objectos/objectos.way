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
package br.com.objectos.code.java.io;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class IndentationTest {

  @Test
  public void start() {
    Indentation it = it();
    test(it, ".");
    it = it.nextLine();
    test(it, ".");
  }

  @Test
  public void block() {
    Indentation it = it().push(Section.BLOCK);
    test(it, ".");
    it = it.nextLine();
    test(it, "  .");
    it = it.nextLine();
    test(it, "  .");
    it = it.push(Section.BLOCK).nextLine();
    test(it, "    .");
    it = it.nextLine();
    test(it, "    .");
    it = it.pop();
    test(it, "  .");
  }

  @Test
  public void statement() {
    Indentation it = it().push(Section.STATEMENT);
    test(it, ".");
    it = it.nextLine();
    test(it, "    .");
    it = it.nextLine();
    test(it, "    .");
    it = it.pop().push(Section.STATEMENT).nextLine();
    test(it, "    .");
    it = it.pop();
    test(it, ".");
  }

  @Test
  public void simulate_whole_file() {
    Indentation it = it();
    test(it, "."); // package
    it = it.nextLine();
    test(it, "."); // import
    it = it.nextLine();
    test(it, "."); // class
    it = it.push(Section.BLOCK).nextLine();
    test(it, "  ."); // field
    it = it.nextLine();
    test(it, "  ."); // method
    it = it.push(Section.BLOCK).nextLine();
    it = it.push(Section.STATEMENT);
    test(it, "    ."); // method body
    it = it.pop().nextLine();
    it = it.push(Section.STATEMENT);
    test(it, "    ."); // if start
    it = it.push(Section.BLOCK).nextLine();
    it = it.push(Section.STATEMENT);
    test(it, "      ."); // if body
  }

  private Indentation it() {
    return Indentation.start();
  }

  private void test(Indentation stack, String expected) {
    CodeWriter w = CodeWriter.forToString();
    stack.acceptCodeWriter(w);
    assertEquals(w.toString() + ".", expected);
  }

}