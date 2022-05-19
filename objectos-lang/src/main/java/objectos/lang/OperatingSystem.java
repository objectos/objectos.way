/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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
package objectos.lang;

/**
 * The operating system the Java application is running on (as parsed from
 * {@code os.*} system properties). The (single) instance of this class is
 * obtained by calling {@link OperatingSystem#get()}.
 *
 * <p>
 * Currently only the Linux operating system is supported by this facility.
 *
 * @since 0.2
 */
public abstract class OperatingSystem {

  private static final OperatingSystem INSTANCE = create();

  private final String osName;

  OperatingSystem(String osName) {
    this.osName = osName;
  }

  /**
   * Returns the single instance of this class.
   *
   * @return the single instance of this class
   */
  public static OperatingSystem get() {
    return INSTANCE;
  }

  private static OperatingSystem create() {
    String osName;
    osName = System.getProperty("os.name");

    if (osName.startsWith("Linux")) {
      return new Linux(osName);
    }

    else if (osName.startsWith("LINUX")) {
      return new Linux(osName);
    }

    else {
      return new UnsupportedOperatingSystem(osName);
    }
  }

  /**
   * Accept method used to implement the visitor pattern.
   *
   * @param <R>
   *        the result type of this operation
   * @param <P>
   *        the type of the additional object
   * @param visitor
   *        the visitor operating on this {@code Os} instance
   * @param p
   *        additional object passed to the visitor
   *
   * @return the value returned from the visitor method
   */
  public abstract <R, P> R acceptOperatingSystemVisitor(OperatingSystemVisitor<R, P> visitor, P p);

  /**
   * Returns the value of the {@code os.name} system property.
   *
   * @return the value of the {@code os.name} system property
   */
  public final String getOsName() {
    return osName;
  }

}
