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
package objectos.selfgen.css.spec;

import java.util.HashMap;
import java.util.Map;
import objectos.lang.Check;
import objectos.selfgen.util.JavaNames;

public enum Primitive {

  ANGLE,

  COLOR,

  DOUBLE,

  IMAGE,

  INT,

  LENGTH,

  PERCENTAGE,

  STRING,

  TIME,

  URI;

  private static final Map<String, Primitive> nameMap = nameMap();

  public static boolean containsName(String value) {
    return nameMap.containsKey(value);
  }

  public static Primitive getByName(String name) {
    Check.argument(containsName(name), name, " not found");

    return nameMap.get(name);
  }

  private static Map<String, Primitive> nameMap() {
    Map<String, Primitive> map = new HashMap<>();

    map.put("angle", ANGLE);
    map.put("color", COLOR);
    map.put("length", LENGTH);
    map.put("percentage", PERCENTAGE);
    map.put("time", TIME);
    map.put("uri", URI);

    map.put("integer", INT);
    map.put("number", DOUBLE);
    map.put("string", STRING);

    return map;
  }

  public final String typeSimpleName() {
    return JavaNames.toValidClassName(name().toLowerCase() + "Type");
  }

}