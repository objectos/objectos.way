/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.http.session;

import java.util.HashMap;
import java.util.Map;
import objectos.http.session.Session;

public final class ObjectoxSession implements Session {

  private final String id;

  private final Map<String, Object> values = new HashMap<>();

  public ObjectoxSession(String id) {
    this.id = id;
  }

  @Override
  public final String id() {
    return id;
  }

  @Override
  public final Object get(String name) {
    synchronized (values) {
      return values.get(name);
    }
  }

  @Override
  public final Object put(String name, Object value) {
    synchronized (values) {
      return values.put(name, value);
    }
  }

  @Override
  public final Object remove(String name) {
    synchronized (values) {
      return values.remove(name);
    }
  }

}