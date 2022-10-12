/*
 * Copyright (C) 2019 Google, Inc.
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
 *
 * ---------
 *
 * Modifications:
 *
 * Copyright (C) 2020 Objectos Software LTDA
 *
 * ObjectosCode is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * ObjectosCode is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ObjectosCode.  If not, see <https://www.gnu.org/licenses/>.
 */
package br.com.objectos.code.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ServiceConfigurationError;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

/**
 * A replacement for {@link java.util.ServiceLoader} that avoids certain
 * long-standing bugs. This simpler implementation does not bother with lazy
 * loading but returns all the service implementations in one list. It makes
 * sure that {@link URLConnection#setUseCaches} is called to turn off jar
 * caching, since that tends to lead to problems in versions before JDK 9.
 *
 * @see <a href="https://github.com/google/auto/issues/718">Issue #718</a>
 * @see <a href=
 *      "https://bugs.openjdk.java.net/browse/JDK-8156014">JDK-8156014</a>
 */
public final class SimpleServiceLoader {

  private static final Charset UTF8 = Charset.forName("UTF-8");

  private SimpleServiceLoader() {}

  public static <T> UnmodifiableList<T> load(Class<? extends T> service) {
    return load(service, service.getClassLoader());
  }

  public static <T> UnmodifiableList<T> load(Class<? extends T> service, ClassLoader loader) {
    String resourceName = "META-INF/services/" + service.getName();

    List<URL> resourceUrls;

    try {
      Enumeration<URL> enumeration;
      enumeration = loader.getResources(resourceName);

      resourceUrls = Collections.list(enumeration);
    } catch (IOException e) {
      throw new ServiceConfigurationError("Could not look up " + resourceName, e);
    }

    GrowableList<T> providers;
    providers = new GrowableList<>();

    for (URL resourceUrl : resourceUrls) {
      try {
        UnmodifiableList<? extends T> urlProviders;
        urlProviders = providersFromUrl(resourceUrl, service, loader);

        providers.addAll(urlProviders);
      } catch (IOException e) {
        throw new ServiceConfigurationError("Could not read " + resourceUrl, e);
      }
    }

    return providers.toUnmodifiableList();
  }

  private static String parseClassName(String line) {
    int hash;
    hash = line.indexOf('#');

    if (hash >= 0) {
      line = line.substring(0, hash);
    }

    line = line.trim();

    if (line.isEmpty()) {
      return null;
    }

    return line;
  }

  private static <T> UnmodifiableList<T> providersFromUrl(
      URL resourceUrl, Class<T> service, ClassLoader loader) throws IOException {
    GrowableList<T> providers;
    providers = new GrowableList<>();

    URLConnection urlConnection;
    urlConnection = resourceUrl.openConnection();

    urlConnection.setUseCaches(false);

    InputStream in;
    in = urlConnection.getInputStream();

    InputStreamReader inReader;
    inReader = new InputStreamReader(in, UTF8);

    BufferedReader reader;
    reader = new BufferedReader(inReader);

    try {
      String line;
      line = reader.readLine();

      while (line != null) {
        String maybeClassName;
        maybeClassName = parseClassName(line);

        line = reader.readLine();

        if (maybeClassName == null) {
          continue;
        }

        String className;
        className = maybeClassName;

        Class<?> c;

        try {
          c = Class.forName(className, false, loader);
        } catch (ClassNotFoundException e) {
          throw new ServiceConfigurationError("Could not load " + className, e);
        }

        if (!service.isAssignableFrom(c)) {
          throw new ServiceConfigurationError(
              "Class " + className + " is not assignable to " + service.getName()
          );
        }

        try {
          Object provider = c.getConstructor().newInstance();
          providers.add(service.cast(provider));
        } catch (Exception e) {
          throw new ServiceConfigurationError("Could not construct " + className, e);
        }
      }

      return providers.toUnmodifiableList();
    } finally {
      reader.close();
    }
  }

}
