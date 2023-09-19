/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
 * Provides the classes for generating HTML using the Java programming language.
 *
 * <h2>{@code HtmlTemplate}</h2>
 *
 * <p>
 * The following Objectos HTML template:
 *
 * {@snippet file = "objectos/html/HtmlTemplateJavadoc.java" region = "main"}
 *
 * <p>
 * Generates the following HTML:
 *
 * <pre>{@code
 *     <!DOCTYPE html>
 *     <html>
 *     <head>
 *     <title>Objectos HTML</title>
 *     </head>
 *     <body>
 *     <p>Hello world!</p>
 *     </body>
 *     </html>
 * }</pre>
 *
 * <h2>Iteration</h2>
 *
 * <p>
 * Use the {@link BaseTemplateDsl#include(objectos.html.tmpl.FragmentAction)
 * include} instruction when you need to generate HTML by iterating over a
 * collection of objects.
 *
 * <p>
 * For example, the following Objectos HTML template:
 *
 * {@snippet file = "objectos/html/HtmlTemplateJavadoc.java" region =
 * "main-iteration"}
 *
 * <p>
 * Generates the following HTML:
 *
 * <pre>{@code
 *     <ul>
 *     <li>Java</li>
 *     <li>Scala</li>
 *     <li>Clojure</li>
 *     <li>Kotlin</li>
 *     </ul>
 * }</pre>
 *
 * <h2>Executing instructions conditionally</h2>
 *
 * <p>
 * You can also use
 * {@link BaseTemplateDsl#include(objectos.html.tmpl.FragmentAction) include}
 * instruction when you need to render or not parts of your template based on a
 * condition.
 *
 * <p>
 * The following template renders an HTML button based on the evaluation of a
 * method which returns a boolean:
 *
 * {@snippet file = "objectos/html/HtmlTemplateJavadoc.java" region =
 * "main-condition01"}
 */
package objectos.html;