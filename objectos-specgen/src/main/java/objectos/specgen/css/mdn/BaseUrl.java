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
import objectos.lang.Check;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BaseUrl {

  private final String value;

  public BaseUrl(String value) {
    this.value = Check.notNull(value, "value == null");
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof BaseUrl)) {
      return false;
    }
    BaseUrl that = (BaseUrl) obj;
    return value.equals(that.value);
  }

  public final PropertyPage getPropertyPage(PropertyAnchor anchor) throws IOException {
    return new PropertyPage(
        get(anchor.getFilename())
    );
  }

  public final ReferencePage getReferencePage() throws IOException {
    return new ReferencePage(
        this,
        get("/Reference")
    );
  }

  public final ReferencePage getReferencePage(Document document) {
    return new ReferencePage(this, document);
  }

  @Override
  public final int hashCode() {
    return value.hashCode();
  }

  void log(String url) {

  }

  private Document get(String slug) throws IOException {
    String url = value + slug;
    log(url);
    return Jsoup.connect(url).ignoreContentType(true).get();
  }

}
