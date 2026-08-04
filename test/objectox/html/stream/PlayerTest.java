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
package objectox.html.stream;

import static org.testng.Assert.assertEquals;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import objectos.html.Html;
import objectos.html.Markup;
import objectox.html.MarkupPojo;
import org.testng.annotations.Test;

public class PlayerTest {

  private void events(Consumer<? super Markup> opts, String expected) {
    final MarkupPojo m;
    m = new Markup.OfHtml();

    opts.accept(m);

    final Html html;
    html = m.compile();

    final String res;
    res = html.events().map(Object::toString).collect(Collectors.joining("\n", "", "\n"));

    assertEquals(res, expected);
  }

  @Test(description = "empty document")
  public void events00() {
    events(
        _ -> {},

        """

        """
    );
  }

  @Test(description = """
  <html></html>
  """)
  public void events01() {
    events(
        m -> m.html(),

        """
        BeginStartTag(html)
        EndStartTag(empty)
        RenderEndTag(html)
        """
    );
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void events02() {
    events(
        m -> m.html(
            m.lang("pt-BR")
        ),

        """
        BeginStartTag(html)
        RenderAttribute(lang, pt-BR)
        EndStartTag(empty)
        RenderEndTag(html)
        """
    );
  }

  @Test(description = """
  <html autofocus></html>
  """)
  public void events03() {
    events(
        m -> m.html(
            Markup.autofocus
        ),

        """
        BeginStartTag(html)
        RenderBooleanAttribute(autofocus)
        EndStartTag(empty)
        RenderEndTag(html)
        """
    );
  }

  @Test(description = """
  <html>
    <head></head>
  </html>
  """)
  public void events04() {
    events(
        m -> m.html(
            m.head()
        ),

        """
        BeginStartTag(html)
        EndStartTag
        BeginStartTag(head)
        EndStartTag(empty)
        RenderEndTag(head)
        RenderEndTag(html)
        """
    );
  }

  @Test(description = """
  <head>
  <meta charset="UTF-8">
  </head>
  """)
  public void events05() {
    events(
        m -> m.head(
            m.meta(m.charset("UTF-8"))
        ),

        """
        BeginStartTag(head)
        EndStartTag
        BeginStartTag(meta)
        RenderAttribute(charset, UTF-8)
        EndStartTag(void)
        RenderEndTag(head)
        """
    );
  }

  @Test(description = """
  <html lang="pt-BR">
    <head></head>
  </html>
  """)
  public void events06() {
    events(
        m -> m.html(
            m.head(),
            m.lang("pt-BR")
        ),

        """
        BeginStartTag(html)
        RenderAttribute(lang, pt-BR)
        EndStartTag
        BeginStartTag(head)
        EndStartTag(empty)
        RenderEndTag(head)
        RenderEndTag(html)
        """
    );
  }

  @Test(description = """
  <!DOCTYPE html>
  <html></html>
  """)
  public void events07() {
    events(
        m -> {
          m.doctype();
          m.html();
        },

        """
        RenderDoctype
        BeginStartTag(html)
        EndStartTag(empty)
        RenderEndTag(html)
        """
    );
  }

  @Test(description = """
  <span>ipsum</span>
  """)
  public void events08() {
    events(
        m -> m.span("ipsum"),

        """
        BeginStartTag(span)
        EndStartTag
        RenderText(ipsum)
        RenderEndTag(span)
        """
    );
  }

}
