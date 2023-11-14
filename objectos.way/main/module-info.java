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
 * Defines the Objectos Way API.
 */
module objectos.way {
	exports objectos.css;
	exports objectos.css.tmpl;
	exports objectos.css.reset;
	exports objectos.http;
	exports objectos.http.media;
	exports objectos.http.server;
	exports objectos.http.util;
	exports objectos.icon;
	exports objectos.js;
	exports objectos.lang;

	requires objectos.html;
	requires objectos.lang.object;
	requires objectos.notes;
	requires objectos.util.array;
	requires objectos.util.list;
	requires objectos.util.map;
	requires objectos.util.set;
}