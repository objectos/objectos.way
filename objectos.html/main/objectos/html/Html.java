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
package objectos.html;

import java.io.IOException;
import objectos.html.internal.InternalCompiledHtml;

/**
 * Immutable and compiled representation of a {@link HtmlTemplate} instance.
 */
public sealed interface Html permits InternalCompiledHtml {

	/**
	 * Writes the HTML represented by this object to the specified
	 * {@link Appendable} instance.
	 *
	 * @param out
	 *        the HTML code will be written to this object
	 *
	 * @throws IOException
	 *         if an I/O error occurs
	 */
	void writeTo(Appendable out) throws IOException;

	/**
	 * Returns the HTML represented by this object.
	 *
	 * @return the HTML represented by this object
	 */
	@Override
	String toString();

}