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
/**
 * Defines interfaces for developing applications that require file system
 * access and provides an implementation for accessing the local file system.
 *
 * <p>
 * Classes in this package will use {@code java.io} for file system access if it
 * is a Java 6 build or will use {@code java.nio.file} if its a Java 7+ build.
 *
 * @since 2
 */
package objectos.fs;