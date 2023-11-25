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
package objectos.fs;

/**
 * A visitor of {@code OperatingSystem} instances.
 *
 * @param <R>
 *        the type of the result of the visit operation
 *
 * @param <P>
 *        the type of the additional value
 *
 * @since 0.2
 */
public interface OperatingSystemVisitor<R, P> {

  /**
   * Visits a {@code Linux} operating system.
   *
   * @param os
   *        the linux instance
   * @param p
   *        an additional value
   *
   * @return the result of the visit operation
   */
  R visitLinux(Linux os, P p);

  /**
   * Visits an unsupported operating system.
   *
   * @param os
   *        the unsupported os instance
   * @param p
   *        an additional value
   *
   * @return the result of the visit operation
   */
  R visitUnsupportedOs(UnsupportedOperatingSystem os, P p);

}