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
package objectos.html;

import static org.testng.Assert.assertEquals;

import java.util.function.Consumer;
import objectox.html.MarkupPojo;
import org.testng.annotations.Test;

public class HtmlTest {

  private void toString(Consumer<? super Markup> opts, String expected) {
    final MarkupPojo m;
    m = new Markup.OfHtml();

    opts.accept(m);

    final Html html;
    html = m.compile();

    final String res;
    res = html.toString();

    assertEquals(res, expected);
  }

  @Test
  public void toString00() {
    toString(
        _ -> {},

        ""
    );
  }

  @Test
  public void toString01() {
    toString(
        m -> m.html(),

        """
        <html></html>
        """
    );
  }

  @Test
  public void toString02() {
    toString(
        m -> m.html(
            m.lang("pt-BR")
        ),

        """
        <html lang="pt-BR"></html>
        """
    );
  }

  @Test
  public void toString03() {
    toString(
        m -> m.html(
            Markup.autofocus
        ),

        """
        <html autofocus></html>
        """
    );
  }

  @Test
  public void toString04() {
    toString(
        m -> m.html(
            m.head()
        ),

        """
        <html>
          <head></head>
        </html>
        """
    );
  }

  @Test
  public void toString05() {
    toString(
        m -> m.head(
            m.meta(m.charset("UTF-8"))
        ),

        """
        <head>
          <meta charset="UTF-8">
        </head>
        """
    );
  }

  @Test
  public void toString06() {
    toString(
        m -> m.html(
            m.head(),
            m.lang("pt-BR")
        ),

        """
        <html lang="pt-BR">
          <head></head>
        </html>
        """
    );
  }

  @Test(description = """
  <!DOCTYPE html>
  <html></html>
  """)
  public void toString07() {
    toString(
        m -> {
          m.doctype();
          m.html();
        },

        """
        <!DOCTYPE html>
        <html></html>
        """
    );
  }

}
