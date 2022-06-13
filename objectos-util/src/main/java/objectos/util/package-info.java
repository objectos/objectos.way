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
 * Provides classes for operating on both primitive and Object arrays.
 *
 * <p>
 * It extends the functionality provided by the {@link java.util.Arrays} class.
 *
 * ---
 *
 * <p>
 * Provides base interfaces and implementations common to all Objectos
 * java.util.Collection modules.
 *
 * ---
 *
 * <p>
 * Provides the Objectos Collections {@link java.util.List} implementations.
 *
 * ---
 *
 * Provides the Objectos {@link java.util.Set} implementations and utilities.
 *
 * <p>
 * The primary goal of this package is to provide a single and unified
 * {@link java.util.Set} API that, ideally, should behave and perform
 * uniformly across multiple Java releases. In particular, it is to provide an
 * API for the Objectos libraries themselves, as they must suport multiple Java
 * releases.
 *
 * <p>
 * A corollary of the former is that it is a non-goal to provide better or
 * or smarter {@link java.util.Set} implementations over the ones from the Java
 * Collections Framework.
 *
 * <p>
 * A secondary goal is to provide a small and concise {@link java.util.Set} API
 * while, at the same time, it should not get in the way of the Objectos
 * programmer.
 *
 * <p>
 * A corollary of the former is that it is a non-goal to satisfy every possible
 * use-case that should arise.
 *
 * <h2>Limitations</h2>
 *
 * <p>
 * The implementations provided by this package are not general-purpose
 * implementations:
 *
 * <ul>
 * <li>they do not permit {@code null} values;</li>
 * <li>some operations specified by {@link java.util.Collection} or by
 * {@link java.util.Set} may not be supported;</li>
 * <li>other operations specified by {@link java.util.Collection} or by
 * {@link java.util.Set} may not be implemented;</li>
 * <li>iterators produced by the classes in this package are NOT<i>fail-fast</i>
 * as defined by {@link java.util.ConcurrentModificationException}; and</li>
 * <li>classes do not implement hash-collision attack mitigations as
 * {@link java.util.HashSet} as {@link java.util.LinkedHashSet} have been
 * implementing since Java 8.</li>
 * </ul>
 *
 * <h2>Null Pointers</h2>
 *
 * <p>
 * Unless otherwise specified, methods in this package will throw a
 * {@link NullPointerException} if given a {@code null} argument.
 *
 * ---
 *
 * Provides the Objectos Collections {@link java.util.Map} implementations and
 * utilities.
 *
 * <h2>Limitations</h2>
 *
 * <p>
 * The implementations provided by this package are not general-purpose
 * implementations:
 *
 * <ul>
 * <li>they do not permit {@code null} keys nor {@code null} values;</li>
 * <li>some operations specified by {@link java.util.Map} may not be
 * supported;</li>
 * <li>other operations specified by {@link java.util.Map} may not be
 * implemented;</li>
 * <li>the "collection view methods" do not return a view of the map. Instead,
 * any mutator method of the returned collection will throw a
 * {@link java.lang.UnsupportedOperationException};</li>
 * <li>iterators produced by the classes in this package, as returned by the
 * "collection view methods", are NOT <i>fail-fast</i> as defined by
 * {@link java.util.ConcurrentModificationException}; and</li>
 * <li>classes do not implement hash-collision attack mitigations as
 * {@link java.util.HashMap} has been implementing since Java 8.</li>
 * </ul>
 *
 * <h2>Null Pointers</h2>
 *
 * <p>
 * Unless otherwise specified, methods in this package will throw a
 * {@link NullPointerException} if given a {@code null} argument.
 *
 */
package objectos.util;