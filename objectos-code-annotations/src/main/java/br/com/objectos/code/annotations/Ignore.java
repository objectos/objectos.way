/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A generic ignore marker annotation.
 *
 * A common use could be to indicate to a framework or a processor that the
 * annotated type, method, field should be (for any reason) ignored.
 *
 * The reason itself, or the target processor can be indicated in the value
 * element.
 */
@Retention(RetentionPolicy.CLASS)
public @interface Ignore {

  /**
   * Message or comment indicating either the recipient of this annotation or
   * the
   * reasoning of the ignoring action.
   */
  String value() default "";

}