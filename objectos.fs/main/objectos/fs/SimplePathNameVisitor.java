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

import java.io.IOException;

/**
 * A simple visitor of {@link PathName} instances.
 *
 * @param <R>
 *        the type of the result of the visit operation
 * @param <P>
 *        the type of the additional value
 *
 * @since 2
 */
public class SimplePathNameVisitor<R, P> implements PathNameVisitor<R, P> {

  /**
   * {@inheritDoc}
   */
  @Override
  public R visitDirectory(Directory directory, P p) throws IOException {
    return defaultAction(directory, p);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public R visitNotFound(ResolvedPath notFound, P p) throws IOException {
    return defaultAction(notFound, p);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public R visitRegularFile(RegularFile file, P p) throws IOException {
    return defaultAction(file, p);
  }

  /**
   * The default action for visit methods.
   *
   * @param pathName
   *        the path name to process
   * @param p
   *        an additional value
   *
   * @return {@code null} unless overridden
   *
   * @throws IOException
   *         may be thrown if overridden
   */
  protected R defaultAction(PathName pathName, P p) throws IOException {
    return null;
  }

}