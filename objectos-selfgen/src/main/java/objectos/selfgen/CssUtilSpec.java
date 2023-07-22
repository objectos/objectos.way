/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen;

import java.io.IOException;
import java.util.List;
import objectos.selfgen.css2.util.CssUtilSelfGen;
import objectos.selfgen.css2.util.Names;
import objectos.selfgen.css2.util.Prefix;

public final class CssUtilSpec extends CssUtilSelfGen {

  private Prefix prefixAll;
  private Prefix prefixSmall;
  private Prefix prefixMedium;
  private Prefix prefixLarge;
  private Prefix prefixXLarge;
  private Prefix prefixXLarge2;

  private List<Prefix> responsive;

  public static void main(String[] args) throws IOException {
    CssUtilSpec spec;
    spec = new CssUtilSpec();

    spec.execute(args);
  }

  @Override
  protected final void definition() {
    // breakpoints
    prefixAll = breakpoint("All", 0);
    prefixSmall = breakpoint("Small", 640);
    prefixMedium = breakpoint("Medium", 768);
    prefixLarge = breakpoint("Large", 1024);
    prefixXLarge = breakpoint("XLarge", 1280);
    prefixXLarge2 = breakpoint("XLarge2", 1536);

    responsive = List.of(
      prefixAll,
      prefixSmall,
      prefixMedium,
      prefixLarge,
      prefixXLarge,
      prefixXLarge2
    );

    // D
    display();

    // F
    flexDirection();

    // J
    justifyContent();

    // M
    minHeight();
  }

  private void display() {
    Names names;
    names = names(
      name("HIDDEN", k("none")),
      name("BLOCK", k("block")),
      name("FLOW_ROOT", k("flowRoot")),
      name("INLINE_BLOCK", k("inlineBlock")),
      name("INLINE", k("inline")),
      name("FLEX", k("flex")),
      name("INLINE_FLEX", k("inlineFlex")),
      name("GRID", k("grid")),
      name("INLINE_GRID", k("inlineGrid")),
      name("TABLE", k("table")),
      name("TABLE_CAPTION", k("tableCaption")),
      name("TABLE_CELL", k("tableCell")),
      name("TABLE_COLUMN", k("tableColumn")),
      name("TABLE_COLUMN_GROUP", k("tableColumnGroup")),
      name("TABLE_FOOTER_GROUP", k("tableFooterGroup")),
      name("TABLE_HEADER_GROUP", k("tableHeaderGroup")),
      name("TABLE_ROW_GROUP", k("tableRowGroup")),
      name("TABLE_ROW", k("tableRow"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("Display"), methods("display"), names);
    }
  }

  private void flexDirection() {
    Names names;
    names = names(
      name("ROW", k("row")),
      name("ROW_REVERSE", k("rowReverse")),
      name("COLUMN", k("column")),
      name("COLUMN_REVERSE", k("columnReverse"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("FlexDirection"), methods("flexDirection"), names);
    }
  }

  private void justifyContent() {
    Names names;
    names = names(
      name("START", k("flexStart")),
      name("CENTER", k("center")),
      name("END", k("flexEnd")),
      name("BETWEEN", k("spaceBetween")),
      name("AROUND", k("spaceAround"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("JustifyContent"), methods("justifyContent"), names);
    }
  }

  private void minHeight() {
    Names names = names(
      name("V0", zero()),
      name("FULL", pct(100)),
      name("SCREEN", vh(100))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("MinHeight"), methods("minHeight"), names);
    }
  }

}