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
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

interface DirectoryJava7 extends DirectoryJavaAny {

  /**
   * Registers this pathname to the specified watch service for the specified
   * events.
   *
   * @param service
   *        the watch service instance
   * @param events
   *        the events to watch for
   *
   * @return a key representing the registration of this object with the given
   *         watch service
   *
   * @throws java.io.IOException
   *         if an I/O error occurs
   *
   * @see java.nio.file.Path#register(java.nio.file.WatchService,
   *      java.nio.file.WatchEvent.Kind...)
   */
  WatchKey register(WatchService service, Kind<?>... events) throws IOException;

  /**
   * Returns a new {@link ResolvedPath} object by resolving the specified path
   * against this directory.
   *
   * @param path
   *        the relative path to a descendant of this directory
   *
   * @return a {@link ResolvedPath} representing the pathname of a descendant of
   *         this directory
   *
   * @throws IllegalArgumentException
   *         if the resolved pathname is not a descendant of this directory
   */
  ResolvedPath resolve(Path path);

}