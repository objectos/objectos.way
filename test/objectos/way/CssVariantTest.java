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
import org.testng.annotations.Test;

public class CssVariantTest {

  @Test(description = "breakpoints first, Css.ClassNameSuffix last")
  public void ordering01() {
    GrowableList<Css.Variant> list;
    list = new GrowableList<>();

    Css.ClassNameSuffix hover = new Css.ClassNameSuffix(2, ":hover");
    Css.ClassNameSuffix focus = new Css.ClassNameSuffix(1, ":focus");
    Css.Breakpoint sm = new Css.Breakpoint(1, "1", "640px");

    list.add(hover);
    list.add(focus);
    list.add(sm);

    List<Css.Variant> res = list.toUnmodifiableList(Comparator.naturalOrder());

    assertEquals(res.get(0), sm);
    assertEquals(res.get(1), focus);
    assertEquals(res.get(2), hover);
  }

  @Test(description = "breakpoints by index")
  public void ordering02() {
    GrowableList<Css.Variant> list;
    list = new GrowableList<>();

    Css.Breakpoint sm = new Css.Breakpoint(1, "1", "640px");
    Css.Breakpoint md = new Css.Breakpoint(2, "2", "768px");
    Css.Breakpoint lg = new Css.Breakpoint(3, "3", "1024px");

    list.add(lg);
    list.add(sm);
    list.add(md);

    List<Css.Variant> res = list.toUnmodifiableList(Comparator.naturalOrder());

    assertEquals(res.get(0), sm);
    assertEquals(res.get(1), md);
    assertEquals(res.get(2), lg);
  }

  @Test(description = "ClassNameFormat first, Css.ClassNameSuffix last")
  public void ordering03() {
    GrowableList<Css.Variant> list;
    list = new GrowableList<>();

    Css.ClassNameSuffix hover = new Css.ClassNameSuffix(2, ":hover");
    Css.ClassNameSuffix focus = new Css.ClassNameSuffix(1, ":focus");
    Css.ClassNameFormat thead = new Css.ClassNameFormat("", " thead");

    list.add(hover);
    list.add(focus);
    list.add(thead);

    List<Css.Variant> res = list.toUnmodifiableList(Comparator.naturalOrder());

    assertEquals(res.get(0), thead);
    assertEquals(res.get(1), focus);
    assertEquals(res.get(2), hover);
  }

  @Test
  public void parse() {
    assertEquals(Css.parseVariant("& thead"), new Css.ClassNameFormat("", " thead"));
  }

}