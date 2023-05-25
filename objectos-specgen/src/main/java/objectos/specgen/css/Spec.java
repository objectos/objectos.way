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
package objectos.specgen.css;

import objectos.lang.Check;
import objectos.util.GrowableMap;
import objectos.util.GrowableSequencedMap;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableMap;

public class Spec {

  public static class Builder {

    private final GrowableMap<String, Property> properties = new GrowableSequencedMap<>();

    public final Builder addProperty(Property property) {
      properties.put(property.name(), property);
      return this;
    }

    public Spec build() {
      return new Spec(
        properties.toUnmodifiableMap()
      );
    }

  }

  private final UnmodifiableMap<String, Property> properties;

  private Spec(UnmodifiableMap<String, Property> properties) {
    this.properties = properties;
  }

  public static Builder builder() {
    return new Builder();
  }

  public final Property getProperty(String name) {
    Check.argument(properties.containsKey(name), name, " not found");

    return properties.get(name);
  }

  public final UnmodifiableList<Property> properties() {
    return UnmodifiableList.copyOf(properties.values());
  }

}