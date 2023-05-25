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
import objectos.specgen.css.Property;
import objectos.specgen.css.Spec;
import objectos.specgen.css.Spec.Builder;

public class MdnCrawler {

  private final BaseUrl baseUrl;

  public MdnCrawler(BaseUrl baseUrl) {
    this.baseUrl = baseUrl;
  }

  public MdnCrawler(String base) {
    baseUrl = new BaseUrl(base);
  }

  public final Spec crawl() {
    Builder b = Spec.builder();

    try {
      ReferencePage referencePage;
      referencePage = baseUrl.getReferencePage();

      Iterable<PropertyAnchor> crawl;
      crawl = referencePage.crawl();

      for (PropertyAnchor anchor : crawl) {
        Optional<PropertyPage> maybePage;
        maybePage = anchor.getPropertyPage();

        if (!maybePage.isPresent()) {
          continue;
        }

        PropertyPage page;
        page = maybePage.get();

        Property property;
        property = page.build();

        b.addProperty(property);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return b.build();
  }

}
