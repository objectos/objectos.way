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
package objectos.specgen.css.mdn;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectos.specgen.css.Property;
import objectos.specgen.css.ValueType;
import objectos.util.UnmodifiableList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.Test;

public class PropertyPageTest {

  @Test
  public void background() throws IOException {
    PropertyPage page = propertyPage("html/MDN/background");

    Property p = page.build();
    assertEquals(p.name(), "background");
    assertEquals(p.formal(), "[ <bg-layer> , ]* <final-bg-layer>");

    UnmodifiableList<ValueType> vt = p.valueTypes();
    assertEquals(vt.size(), 47);

    ValueType vt0 = vt.get(0);
    assertEquals(
      vt0,
      new ValueType(
        "<bg-layer>",
        "<bg-image> || <bg-position> [ / <bg-size> ]? || <repeat-style> || <attachment> || <box> || <box>"
      )
    );
  }

  @Test
  public void bottom() throws IOException {
    PropertyPage page = propertyPage("html/MDN/bottom");
    Property p = page.build();

    assertEquals(
      p,
      new Property(
        "bottom",
        "<length> | <percentage> | auto",
        UnmodifiableList.of()
      )
    );
  }

  private PropertyPage propertyPage(String resourceName) throws IOException {
    String html = Resource.readString(resourceName);

    Document document = Jsoup.parse(html);

    return new PropertyPage(document);
  }

}
