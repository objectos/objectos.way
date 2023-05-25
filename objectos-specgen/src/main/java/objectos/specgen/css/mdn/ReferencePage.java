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

import objectos.util.GrowableList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReferencePage {

  private final BaseUrl baseUrl;

  private final Document fragment;

  public ReferencePage(BaseUrl baseUrl, Document fragment) {
    this.baseUrl = baseUrl;
    this.fragment = fragment;
  }

  public final Iterable<PropertyAnchor> crawl() {
    GrowableList<PropertyAnchor> result;
    result = new GrowableList<>();

    Elements elements = fragment.select("div.index code");

    for (Element element : elements) {
      if (filter(element)) {
        PropertyAnchor anchor;
        anchor = getPropertyAnchor(element);

        result.add(anchor);
      }
    }

    return result;
  }

  private boolean filter(Element code) {
    String text = code.text();
    char firstChar = text.charAt(0);

    Element anchor = code.parent();
    String href = anchor.attr("href");

    return Character.isLetter(firstChar)
        && Character.isLowerCase(firstChar)
        && !text.contains("(")
        && !href.contains("#");
  }

  private PropertyAnchor getPropertyAnchor(Element code) {
    return PropertyAnchor.ofCode(baseUrl, code);
  }

}
