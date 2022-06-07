/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
 * Provides utilities for working with instances whose type is of the
 * {@code java.lang} package; also provides a type-safe note sink API.
 *
 * <h2>{@code java.lang} utilities</h2>
 *
 * <p>
 * The utilities are listed below grouped by the {@code java.lang} type
 * it relates to
 *
 * <h3>{@code Object}</h3>
 *
 * <ul>
 * <li>{@link objectos.lang.Check}
 * <li>{@link objectos.lang.Equals}
 * <li>{@link objectos.lang.HashCode}
 * <li>{@link objectos.lang.ToString}
 * <li>{@link objectos.lang.ToStringObject}
 * </ul>
 *
 * <h3>{@code String}</h3>
 *
 * <ul>
 * <li>{@link objectos.lang.RandomString}
 * <li>{@link objectos.lang.StringConversion}
 * <li>{@link objectos.lang.StringConverter}
 * </ul>
 *
 * <h3>{@code System}</h3>
 *
 * <ul>
 * <li>{@link objectos.lang.OperatingSystem}
 * <li>{@link objectos.lang.OperatingSystemVisitor}
 * </ul>
 *
 * <h3>{@code Throwable}</h3>
 *
 * <ul>
 * <li>{@link objectos.lang.Throwables}
 * </ul>
 *
 * <h2>Note sink API</h2>
 *
 * <p>
 * Additionally, this package provides a type-safe note sink API. The main
 * reference can be found in classes listed below:
 *
 * <ul>
 * <li>{@link objectos.lang.Note}
 * <li>{@link objectos.lang.NoteSink}
 * </ul>
 *
 * <h2>Handling of {@code null} arguments</h2>
 *
 * <p>
 * Unless otherwise specified, methods in this package will throw a
 * {@link NullPointerException} if given a {@code null} argument.
 *
 * @since 0.2
 */
package objectos.lang;