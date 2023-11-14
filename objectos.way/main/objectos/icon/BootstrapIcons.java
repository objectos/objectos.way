/*
 * Code from the Bootstrap Icons project.
 *
 * (https://github.com/twbs/icons/blob/main/LICENSE)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2019-2023 The Bootstrap Authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Modifications:
 *
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.icon;

import objectos.html.BaseTemplateDsl;
import objectos.html.HtmlComponent;
import objectos.html.tmpl.Api.Element;
import objectos.html.tmpl.Api.SvgInstruction;

/**
 * Provides SVG icons from the
 * <a href="https://github.com/twbs/icons">Bootstrap</a> project.
 */
public class BootstrapIcons extends HtmlComponent {

	/**
	 * Creates a new instance of this class bound to the specified template.
	 *
	 * @param parent
	 *        the template in which icons will be drawn
	 */
	public BootstrapIcons(BaseTemplateDsl parent) {
		super(parent);
	}

	/**
	 * Renders the
	 * <a href="https://icons.getbootstrap.com/icons/envelope/">envelope</a> icon.
	 *
	 * @return the {@code svg} element
	 */
	public final Element envelope(SvgInstruction... contents) {
		// @formatter:off
    return icon(
      flatten(contents),
      path(d("M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4Zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2Zm13 2.383-4.708 2.825L15 11.105V5.383Zm-.034 6.876-5.64-3.471L8 9.583l-1.326-.795-5.64 3.47A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.741ZM1 11.105l4.708-2.897L1 5.383v5.722Z"))
    );
    // @formatter:on
	}

	/**
	 * Renders the
	 * <a href="https://icons.getbootstrap.com/icons/envelope-fill/">envelope
	 * fill</a>
	 * icon.
	 *
	 * @return the {@code svg} element
	 */
	public final Element envelopeFill(SvgInstruction... contents) {
		// @formatter:off
    return icon(
      flatten(contents),
      path(d("M.05 3.555A2 2 0 0 1 2 2h12a2 2 0 0 1 1.95 1.555L8 8.414.05 3.555ZM0 4.697v7.104l5.803-3.558L0 4.697ZM6.761 8.83l-6.57 4.027A2 2 0 0 0 2 14h12a2 2 0 0 0 1.808-1.144l-6.57-4.027L8 9.586l-1.239-.757Zm3.436-.586L16 11.801V4.697l-5.803 3.546Z"))
    );
    // @formatter:on
	}

	/**
	 * Renders the
	 * <a href="https://icons.getbootstrap.com/icons/github/">GitHub</a>
	 * icon.
	 *
	 * @return the {@code svg} element
	 */
	public final Element github(SvgInstruction... contents) {
		// @formatter:off
    return icon(
      flatten(contents),
      path(d("M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.012 8.012 0 0 0 16 8c0-4.42-3.58-8-8-8z"))
    );
    // @formatter:on
	}

	/**
	 * Renders the
	 * <a href="https://icons.getbootstrap.com/icons/linkedin/">Linkedin</a>
	 * icon.
	 *
	 * @return the {@code svg} element
	 */
	public final Element linkedin(SvgInstruction... contents) {
		// @formatter:off
    return icon(
      flatten(contents),
      path(d("M0 1.146C0 .513.526 0 1.175 0h13.65C15.474 0 16 .513 16 1.146v13.708c0 .633-.526 1.146-1.175 1.146H1.175C.526 16 0 15.487 0 14.854V1.146zm4.943 12.248V6.169H2.542v7.225h2.401zm-1.2-8.212c.837 0 1.358-.554 1.358-1.248-.015-.709-.52-1.248-1.342-1.248-.822 0-1.359.54-1.359 1.248 0 .694.521 1.248 1.327 1.248h.016zm4.908 8.212V9.359c0-.216.016-.432.08-.586.173-.431.568-.878 1.232-.878.869 0 1.216.662 1.216 1.634v3.865h2.401V9.25c0-2.22-1.184-3.252-2.764-3.252-1.274 0-1.845.7-2.165 1.193v.025h-.016a5.54 5.54 0 0 1 .016-.025V6.169h-2.4c.03.678 0 7.225 0 7.225h2.4z"))
    );
    // @formatter:on
	}

	/**
	 * Renders the
	 * <a href="https://icons.getbootstrap.com/icons/three-dots/">three-dots</a>
	 * icon.
	 *
	 * @return the {@code svg} element
	 */
	public final Element threeDots(SvgInstruction... contents) {
		// @formatter:off
    return icon(
      flatten(contents),
      path(d("M3 9.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3z"))
    );
    // @formatter:on
	}

	/**
	 * Renders the
	 * <a href="https://icons.getbootstrap.com/icons/twitter/">Twitter</a>
	 * icon.
	 *
	 * @return the {@code svg} element
	 */
	public final Element twitter(SvgInstruction... contents) {
		// @formatter:off
    return icon(
      flatten(contents),
      path(d("M5.026 15c6.038 0 9.341-5.003 9.341-9.334 0-.14 0-.282-.006-.422A6.685 6.685 0 0 0 16 3.542a6.658 6.658 0 0 1-1.889.518 3.301 3.301 0 0 0 1.447-1.817 6.533 6.533 0 0 1-2.087.793A3.286 3.286 0 0 0 7.875 6.03a9.325 9.325 0 0 1-6.767-3.429 3.289 3.289 0 0 0 1.018 4.382A3.323 3.323 0 0 1 .64 6.575v.045a3.288 3.288 0 0 0 2.632 3.218 3.203 3.203 0 0 1-.865.115 3.23 3.23 0 0 1-.614-.057 3.283 3.283 0 0 0 3.067 2.277A6.588 6.588 0 0 1 .78 13.58a6.32 6.32 0 0 1-.78-.045A9.344 9.344 0 0 0 5.026 15z"))
    );
    // @formatter:on
	}

	/**
	 * Renders the
	 * <a href="https://icons.getbootstrap.com/icons/twitter-x/">Twitter X</a>
	 * icon.
	 *
	 * @return the {@code svg} element
	 */
	public final Element twitterX(SvgInstruction... contents) {
		// @formatter:off
    return icon(
      flatten(contents),
      path(d("M12.6.75h2.454l-5.36 6.142L16 15.25h-4.937l-3.867-5.07-4.425 5.07H.316l5.733-6.57L0 .75h5.063l3.495 4.633L12.601.75Zm-.86 13.028h1.36L4.323 2.145H2.865l8.875 11.633Z"))
    );
    // @formatter:on
	}

	/**
	 * Renders the <a href="https://icons.getbootstrap.com/icons/x/">x</a> icon.
	 *
	 * @return the {@code svg} element
	 */
	public final Element x(SvgInstruction... contents) {
		// @formatter:off
    return icon(
      flatten(contents),
      path(d("M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"))
    );
    // @formatter:on
	}

	private Element icon(SvgInstruction... contents) {
		return svg(
				xmlns("http://www.w3.org/2000/svg"),
				width("16"),
				height("16"),
				viewBox("0 0 16 16"),
				fill("currentColor"),

				flatten(contents)
		);
	}

}