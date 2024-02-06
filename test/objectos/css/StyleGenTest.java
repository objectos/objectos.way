/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.css;

import static org.testng.Assert.assertEquals;

import objectos.html.HtmlTemplate;
import objectos.way.TestingNoteSink;
import org.testng.annotations.Test;

public class StyleGenTest {

  @Test
  public void display() {
    class Subject extends HtmlTemplate {
      @Override
      protected void definition() {
        div(
            className("block"),
            className("inline-block"),
            className("inline"),
            className("flex"),
            className("inline-flex"),
            className("table"),
            className("inline-table"),
            className("table-caption"),
            className("table-cell"),
            className("table-column"),
            className("table-column-group"),
            className("table-footer-group"),
            className("table-header-group"),
            className("table-row-group"),
            className("table-row"),
            className("flow-root"),
            className("grid"),
            className("inline-grid"),
            className("contents"),
            className("list-item"),
            className("hidden")
        );
      }
    }

    test(
        Subject.class,

        """
        .block { display: block }
        .inline-block { display: inline-block }
        .inline { display: inline }
        .flex { display: flex }
        .inline-flex { display: inline-flex }
        .table { display: table }
        .inline-table { display: inline-table }
        .table-caption { display: table-caption }
        .table-cell { display: table-cell }
        .table-column { display: table-column }
        .table-column-group { display: table-column-group }
        .table-footer-group { display: table-footer-group }
        .table-header-group { display: table-header-group }
        .table-row-group { display: table-row-group }
        .table-row { display: table-row }
        .flow-root { display: flow-root }
        .grid { display: grid }
        .inline-grid { display: inline-grid }
        .contents { display: contents }
        .list-item { display: list-item }
        .hidden { display: none }
        """
    );
  }

  @Test
  public void flexDirection() {
    class Subject extends HtmlTemplate {
      @Override
      protected void definition() {
        div(
            className("flex-row"),
            className("flex-row-reverse"),
            className("flex-col"),
            className("flex-col-reverse")
        );
      }
    }

    test(
        Subject.class,

        """
        .flex-row { flex-direction: row }
        .flex-row-reverse { flex-direction: row-reverse }
        .flex-col { flex-direction: column }
        .flex-col-reverse { flex-direction: column-reverse }
        """
    );
  }

  private void test(Class<?> type, String expected) {
    WayStyleGen gen;
    gen = new WayStyleGen();

    gen.noteSink(TestingNoteSink.INSTANCE);

    gen.scan(type);

    assertEquals(gen.generate(), expected);
  }

}