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

import java.io.IOException;
import java.util.Optional;
import org.jsoup.nodes.Element;

public class PropertyAnchor {

  private final BaseUrl baseUrl;
  private final String property;
  private final String href;

  PropertyAnchor(BaseUrl baseUrl, String property, String href) {
    this.baseUrl = baseUrl;
    this.property = property;
    this.href = href;
  }

  static PropertyAnchor ofCode(BaseUrl baseUrl, Element code) {
    return new PropertyAnchor(
        baseUrl,
        code.text(),
        code.parent().attr("href")
    );
  }

  public final Optional<PropertyPage> getPropertyPage() {
    try {
      PropertyPage page = baseUrl.getPropertyPage(this);
      if (page.isValid()) {
        return Optional.of(page);
      } else {
        System.out.println("Invalid page @ " + page.name());
        return Optional.empty();
      }
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  public final String property() {
    return property;
  }

  public final String href() {
    return href;
  }

  final String getFilename() {
    return href.substring(href.lastIndexOf('/'));
  }

}
