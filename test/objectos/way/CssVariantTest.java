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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.util.Comparator;
import java.util.List;
import objectos.util.list.GrowableList;
import objectos.way.CssVariant.AppendTo;
import objectos.way.CssVariant.Breakpoint;
import objectos.way.CssVariant.ClassNameFormat;
import org.testng.annotations.Test;

public class CssVariantTest {

  @Test(description = "breakpoints first, AppendTo last")
  public void ordering01() {
    GrowableList<CssVariant> list;
    list = new GrowableList<>();

    AppendTo hover = new AppendTo(2, ":hover");
    AppendTo focus = new AppendTo(1, ":focus");
    Breakpoint sm = new Breakpoint(1, "1", "640px");

    list.add(hover);
    list.add(focus);
    list.add(sm);

    List<CssVariant> res = list.toUnmodifiableList(Comparator.naturalOrder());

    assertEquals(res.get(0), sm);
    assertEquals(res.get(1), focus);
    assertEquals(res.get(2), hover);
  }

  @Test(description = "breakpoints by index")
  public void ordering02() {
    GrowableList<CssVariant> list;
    list = new GrowableList<>();

    Breakpoint sm = new Breakpoint(1, "1", "640px");
    Breakpoint md = new Breakpoint(2, "2", "768px");
    Breakpoint lg = new Breakpoint(3, "3", "1024px");

    list.add(lg);
    list.add(sm);
    list.add(md);

    List<CssVariant> res = list.toUnmodifiableList(Comparator.naturalOrder());

    assertEquals(res.get(0), sm);
    assertEquals(res.get(1), md);
    assertEquals(res.get(2), lg);
  }

  @Test(description = "ClassNameFormat first, AppendTo last")
  public void ordering03() {
    GrowableList<CssVariant> list;
    list = new GrowableList<>();

    AppendTo hover = new AppendTo(2, ":hover");
    AppendTo focus = new AppendTo(1, ":focus");
    ClassNameFormat thead = new ClassNameFormat("", " thead");

    list.add(hover);
    list.add(focus);
    list.add(thead);

    List<CssVariant> res = list.toUnmodifiableList(Comparator.naturalOrder());

    assertEquals(res.get(0), thead);
    assertEquals(res.get(1), focus);
    assertEquals(res.get(2), hover);
  }

  @Test
  public void parse() {
    assertEquals(CssVariant.parse("& thead"), new CssVariant.ClassNameFormat("", " thead"));
  }

}