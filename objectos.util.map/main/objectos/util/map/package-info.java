/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
 * Provides special-purpose {@link java.util.Map} implementations.
 *
 * <p>
 * The primary goal of this package is to provide a special-purpose (as opposed
 * to general-purpose) collections API for the Objectos libraries themselves.
 *
 * <p>
 * It is a non-goal to provide <em>better</em> or <em>"smarter"</em>
 * implementations over the ones from the Java Collections Framework.
 *
 * <p>
 * A secondary goal is to provide a small and concise collections API while, at
 * the same time, it should not get in the way of the Objectos programmer.
 *
 * <p>
 * It is a non-goal to satisfy every possible use-case that should arise.
 *
 * <h2>Limitations</h2>
 *
 * <p>
 * As mentioned, the implementations provided by this package are not
 * general-purpose implementations. In particular:
 *
 * <ul>
 * <li>they do not permit {@code null} values;</li>
 * <li>some operations specified either by {@link java.util.Collection},
 * {@link java.util.List}, {@link java.util.Set} or {@link java.util.Map} may
 * not be supported;</li>
 * <li>other operations specified either by {@link java.util.Collection},
 * {@link java.util.List}, {@link java.util.Set} or {@link java.util.Map} may
 * not be implemented;</li>
 * <li>iterators produced by the classes in this package are NOT
 * <i>fail-fast</i> as defined by
 * {@link java.util.ConcurrentModificationException}; and</li>
 * <li>classes do not implement hash-collision attack mitigations as
 * {@link java.util.HashSet} as {@link java.util.HashMap} have been
 * implementing since Java 8 (see <a href="https://openjdk.org/jeps/180">JEP
 * 180</a>).</li>
 * </ul>
 *
 * <h2>Null Pointers</h2>
 *
 * <p>
 * Unless otherwise specified, methods in this package will throw a
 * {@link NullPointerException} if given a {@code null} argument.
 *
 * @since 0.3
 */
package objectos.util.map;