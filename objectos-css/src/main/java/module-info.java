/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
 * Defines the Objectos CSS API.
 *
 * @since 0.7.0
 */
module objectos.css {
  exports objectos.css;
  exports objectos.css.config.framework;
  exports objectos.css.function;
  exports objectos.css.keyword;
  exports objectos.css.om;
  exports objectos.css.parser;
  exports objectos.css.select;
  exports objectos.css.property;
  exports objectos.css.sheet;
  exports objectos.css.tmpl;
  exports objectos.css.type;
  exports objectos.css.util;

  requires objectos.html;
  requires objectos.lang;
  requires objectos.util;
}