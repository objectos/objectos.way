/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
(function() {

	"use strict";

	function clickListener(event) {
		const target = event.target;

		const dataset = target.dataset;

		const data = dataset.wayClick;

		if (!data) {
			return;
		}

		const way = JSON.parse(data);

		if (!Array.isArray(way)) {
			return;
		}

		executeActions(way);
	}

	function submitListener(event) {
		const target = event.target;

		// verify we have all of the required properties
		const tagName = target.tagName;

		if (tagName !== "FORM") {
			return;
		}

		const action = target.getAttribute("action");

		if (!action) {
			return;
		}

		const method = target.getAttribute("method");

		if (!method) {
			return;
		}

		// this is possibly a way form, we shouldn't submit it
		event.preventDefault();

		const xhr = new XMLHttpRequest();

		xhr.open(method.toUpperCase(), action, true);

		xhr.onload = (_) => {
			const contentType = xhr.getResponseHeader('content-type');

			if (!contentType || !contentType.startsWith("application/json")) {
				return;
			}

			const data = JSON.parse(xhr.response);

			if (!Array.isArray(data)) {
				return;
			}

			executeActions(data);
		}

		const formData = new FormData(target);

		const enctype = target.getAttribute("enctype");

		if (enctype && enctype === "multipart/form-data") {
			xhr.send(formData);
		} else {
			const params = new URLSearchParams(formData);

			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

			xhr.send(params.toString());
		}
	}

	function executeActions(way) {
		var html;

		for (const obj of way) {
			const cmd = obj.cmd;

			if (!cmd) {
				continue;
			}

			switch (cmd) {
				case "html": {
					const value = obj.value;

					if (!value) {
						break;
					}

					const parser = new DOMParser();

					html = parser.parseFromString(value, "text/html");
					
					break;
				}

				case "location-href": {
					const value = obj.value;

					if (!value) {
						break;
					}

					window.location.href = value;
					
					break;
				}

				case "replace": {
					const id = obj.id;

					if (!id) {
						break;
					}

					if (!html) {
						break;
					}

					const old = document.getElementById(id);

					if (!old) {
						break;
					}

					const replacement = html.getElementById(id);

					if (!replacement) {
						break;
					}

					old.replaceWith(replacement);
					
					break;
				}

				case "replace-class": {
					const args = obj.args;

					if (!args) {
						break;
					}

					if (args.length !== 3) {
						break;
					}

					const id = args[0];

					const el = document.getElementById(id);

					if (!el) {
						break;
					}

					const classList = el.classList;

					const classA = args[1];

					const classB = args[2];

					classList.replace(classA, classB);

					break;
				}

				case "swap": {
					const args = obj.args;

					if (!args) {
						break;
					}

					if (args.length !== 2) {
						break;
					}

					const id = args[0];

					const el = document.getElementById(id);

					if (!el) {
						break;
					}

					const mode = args[1];

					switch (mode) {
						case "innerHTML": { el.innerHTML = resp; }

						case "outerHTML": { el.outerHTML = resp; }
					}

					break;
				}
			}

		}
	}

	function domLoaded() {
		const body = document.body;

		body.addEventListener("click", clickListener);
		body.addEventListener("submit", submitListener);
	}

	window.addEventListener("DOMContentLoaded", domLoaded);

})();