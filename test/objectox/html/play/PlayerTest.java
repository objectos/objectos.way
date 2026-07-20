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

import java.util.function.Consumer;
import objectos.html.Markup;
import objectox.html.MarkupPojo;
import org.testng.annotations.Test;

public class PlayerTest {

  private void test(Consumer<? super Markup> html, String expected) {
    final MarkupPojo m;
    m = new Markup.OfHtml();

    html.accept(m);

    final Player subject;
    subject = m.toPlayer();

    final StringBuilder out;
    out = new StringBuilder();

    if (subject.hasNext()) {
      out.append(subject.next());

      while (subject.hasNext()) {
        out.append('\n');
        out.append(subject.next());
      }
    }

    out.append('\n');

    final String res;
    res = out.toString();

    assertEquals(res, expected);
  }

  @Test(description = "empty document")
  public void testCase00() {
    test(
        _ -> {},

        """
        BeginDocument
        EndDocument
        """
    );
  }

  @Test(description = """
  <html></html>
  """)
  public void testCase01() {
    test(
        m -> m.html(),

        """
        BeginDocument
        BeginStartTag(html)
        EndStartTag
        EndTag(html)
        EndDocument
        """
    );
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void testCase02() {
    test(
        m -> m.html(
            m.lang("pt-BR")
        ),

        """
        BeginDocument
        BeginStartTag(html)
        Attribute(lang, pt-BR)
        EndStartTag
        EndTag(html)
        EndDocument
        """
    );
  }

  @Test(description = """
  <html class="no-js" lang="pt-BR"></html>
  """)
  public void testCase03() {
    test(
        m -> m.html(
            m.className("no-js"),
            m.lang("pt-BR")
        ),

        """
        BeginDocument
        BeginStartTag(html)
        Attribute(class, no-js)
        Attribute(lang, pt-BR)
        EndStartTag
        EndTag(html)
        EndDocument
        """
    );
  }

  @Test(enabled = false, description = """
  <html><head></head></html>
  """)
  public void testCase04() {
    test(
        m -> m.html(
            m.head()
        ),

        """
        BeginDocument
        BeginStartTag(html)
        EndStartTag
        BeginStartTag(head)
        EndStartTag
        EndTag(head)
        EndTag(html)
        EndDocument
        """
    );
  }

}
