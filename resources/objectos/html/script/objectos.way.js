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

		if (target instanceof HTMLAnchorElement) {

			if (target.origin !== window.location.origin) {
				return;
			}

			if (target.pathname === window.location.pathname) {
				return;
			}

			event.preventDefault();

			executeLocation(target.href);

		}

		else {

			const dataset = target.dataset;

			const data = dataset.click;

			if (!data) {
				return;
			}

			const way = JSON.parse(data);

			if (!Array.isArray(way)) {
				return;
			}

			executeActions(way);

		}
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

		const xhr = createXhr(method, action);

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

	function createXhr(method, url) {
		const xhr = new XMLHttpRequest();

		xhr.open(method.toUpperCase(), url, true);

		xhr.onload = (_) => {
			if (xhr.status === 200) {

				const contentType = xhr.getResponseHeader("content-type");

				if (!contentType) {
					return;
				}

				if (contentType.startsWith("application/json")) {
					const data = JSON.parse(xhr.response);

					if (!Array.isArray(data)) {
						return;
					}

					executeActions(data);
				}

				else if (contentType.startsWith("text/html")) {
					executeHtml(xhr.response);
				}

			}

			else if (xhr.status === 302) {

				const location = xhr.getResponseHeader("location")

				if (!location) {
					return;
				}

				executeLocation(location);

			}
		}

		return xhr;
	}

	function executeActions(way) {
		for (const obj of way) {

			const cmd = obj.cmd;

			if (!cmd) {
				continue;
			}

			switch (cmd) {
				case "html": {
					executeHtml(obj.value);

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
			}

		}
	}

	function executeHtml(value) {
		if (!value) {
			return;
		}

		const parser = new DOMParser();

		const newContent = parser.parseFromString(value, "text/html");

		// handle title

		const newTitle = newContent.querySelector("title");

		if (newTitle) {
			document.title = newTitle.innerText;
		}

		// handle frames
		const newFrames = newContent.querySelectorAll("[data-frame]");

		const newNameMap = new Map();

		for (const frame of newFrames) {
			const name = frameName(frame);

			if (name) {
				newNameMap.set(name, frame);
			}
		}

		const frames = document.querySelectorAll("[data-frame]");

		const replaced = new Set();

		outer: for (const frame of frames) {
			const name = frameName(frame);

			if (!name) {
				continue;
			}

			for (const parent of replaced) {
				if (parent.contains(frame)) {
					continue outer;
				}
			}

			const maybe = newNameMap.get(name);

			if (!maybe) {
				// this frame does not exist in the new data
				frame.remove();

				continue;
			}

			const newFrame = maybe;

			const oldValue = frameValue(frame);

			const newValue = frameValue(newFrame);

			if (!oldValue || !newValue) {
				continue;
			}

			if (oldValue !== newValue) {
				replaced.add(frame);

				frame.replaceWith(newFrame);
			}
		}
	}

	function executeLocation(location) {
		const xhr = createXhr("GET", location);

		xhr.send();
	}

	function frameName(frame) {
		const dataset = frame.dataset;

		if (!dataset) {
			return null;
		}

		return dataset.frame;
	}

	function frameValue(frame) {
		const dataset = frame.dataset;

		if (!dataset) {
			return null;
		}

		return dataset.frameValue;
	}

	function domLoaded() {
		const body = document.body;

		body.addEventListener("click", clickListener);
		body.addEventListener("submit", submitListener);
	}

	window.addEventListener("DOMContentLoaded", domLoaded);

})();