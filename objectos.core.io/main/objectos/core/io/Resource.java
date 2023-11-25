/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.core.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import objectos.lang.object.Check;
import objectos.lang.object.ToString;

/**
 * A {@link ClassLoader} resource.
 *
 * <p>
 * This class encapsulates an {@link java.net.URL} obtained from a class loader
 * and implements {@link InputStreamSource} and {@link ReaderSource} so it can
 * be used with the {@link Copy} and {@link Read} classes.
 *
 * @since 2
 */
public final class Resource implements InputStreamSource, ReaderSource, ToString.Formattable {

  private final URL url;

  private Resource(URL url) {
    this.url = url;
  }

  /**
   * Returns a new resource instance relative to the specified context class
   * with the specified name.
   *
   * @param contextClass
   *        the class to use for obtaining the resource
   * @param resourceName
   *        the name of the resource
   *
   * @return a new resource instance relative to the specified context class
   *         with the specified name
   *
   * @throws IllegalArgumentException
   *         if the resource cannot be found
   *
   * @see Class#getResource(String)
   */
  public static Resource getResource(Class<?> contextClass, String resourceName) {
    Check.notNull(contextClass, "contextClass == null");
    Check.notNull(resourceName, "resourceName == null");

    URL url;
    url = contextClass.getResource(resourceName);

    checkUrl(url, resourceName);

    return new Resource(url);
  }

  /**
   * Returns a new resource instance from the context class loader with the
   * specified name.
   *
   * @param resourceName
   *        the name of the resource
   *
   * @return a new resource instance from the context class loader with the
   *         specified name
   *
   * @throws IllegalArgumentException
   *         if the resource name starts with a '/' character or if the resource
   *         cannot be found
   *
   * @see ClassLoader#getResource(String)
   */
  public static Resource getResource(String resourceName) {
    Check.notNull(resourceName, "resourceName == null");
    Check.argument(resourceName.charAt(0) != '/', "resourceName must not start with a '/'");

    Thread currentThread;
    currentThread = Thread.currentThread();

    ClassLoader loader;
    loader = currentThread.getContextClassLoader();

    URL url;
    url = loader.getResource(resourceName);

    checkUrl(url, resourceName);

    return new Resource(url);
  }

  private static void checkUrl(URL url, String resourceName) {
    if (url == null) {
      throw new IllegalArgumentException("Resource " + resourceName + " not found.");
    }
  }

  /**
   * Compares the specified object with this resource for equality. Returns
   * {@code true} if and only if both objects refer to the same URL.
   *
   * @param obj
   *        the object to be compared for equality with this resource
   *
   * @return {@code true} if the specified object is equal to this resource
   */
  @Override
  public final boolean equals(Object obj) {
    return obj == this
        || obj instanceof Resource && equals0((Resource) obj);
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} a string representation of this resource.
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   */
  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this,
        "", url
    );
  }

  /**
   * Returns the hash code value of this resource.
   *
   * @return the hash code value of this resource
   */
  @Override
  public final int hashCode() {
    return url.hashCode();
  }

  /**
   * Returns a new {@link InputStream} for reading from this resource.
   *
   * @return a new {@link InputStream} for reading from this resource
   *
   * @throws IOException
   *         if an I/O exception occurs
   */
  @Override
  public final InputStream openInputStream() throws IOException {
    return url.openStream();
  }

  /**
   * Returns a new {@link Reader} with the specified charset for reading from
   * this resource.
   *
   * @param charset
   *        the charset to use for decoding
   *
   * @return a new {@link Reader} with the specified charset
   *
   * @throws IOException
   *         if an I/O exception occurs
   */
  @Override
  public final Reader openReader(Charset charset) throws IOException {
    InputStream inputStream;
    inputStream = openInputStream();

    InputStreamReader reader;
    reader = new InputStreamReader(inputStream, charset);

    return new BufferedReader(reader);
  }

  /**
   * Returns a string representation of this resource.
   *
   * @return a string representation of this resource
   */
  @Override
  public final String toString() {
    return ToString.of(this);
  }

  /**
   * Returns a {@link java.net.URI} equivalent to this resource.
   *
   * @return a URI instance equivalent to this resource.
   */
  public final URI toUri() {
    try {
      return url.toURI();
    } catch (URISyntaxException e) {
      AssertionError error;
      error = new AssertionError("Should not happen: URL obtained from a class loader");

      error.initCause(e);

      throw error;
    }
  }

  private boolean equals0(Resource that) {
    return url.equals(that.url);
  }

}