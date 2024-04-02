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
(function(globals) {

	"use strict";

	globals.Way = {
		disableHistory: false
	}

	function onClick(event) {
		const target = event.target;

		const dataset = target.dataset;

		const data = dataset.click;

		if (data) {
			const way = JSON.parse(data);

			executeActions(way);

			return;
		}

		let maybeAnchor = target;

		while (maybeAnchor instanceof Node) {
			if (maybeAnchor instanceof HTMLAnchorElement) {
				const anchor = maybeAnchor;

				if (anchor.origin !== window.location.origin) {
					return;
				}

				event.preventDefault();

				executeLocation(anchor.href);

				return;

			} else {
				maybeAnchor = maybeAnchor.parentNode;
			}
		}

	}

	function onInput(event) {
		const target = event.target;

		const dataset = target.dataset;

		const data = dataset.onInput;

		if (!data) {
			return;
		}

		const way = JSON.parse(data);

		executeActions(way);
	}

	function submitListener(event) {
		const target = event.target;

		// verify we have all of the required properties
		const tagName = target.tagName;

		if (tagName !== "FORM") {
			return;
		}

		let action = target.getAttribute("action");

		if (!action) {
			return;
		}

		let method = target.getAttribute("method");

		if (!method) {
			return;
		}

		method = method.toUpperCase();

		// this is possibly a way form, we shouldn't submit it
		event.preventDefault();

		const formData = new FormData(target);

		if (method === "GET") {
			const params = new URLSearchParams(formData);

			action = action + "?" + params;

			const xhr = createXhr(method, action);

			xhr.send();
		}

		else {
			const xhr = createXhr(method, action);

			const enctype = target.getAttribute("enctype");

			if (enctype && enctype === "multipart/form-data") {
				xhr.send(formData);
			} else {
				const params = new URLSearchParams(formData);

				xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

				xhr.send(params.toString());
			}
		}
	}

	function createXhr(method, url) {
		const xhr = new XMLHttpRequest();

		xhr.open(method, url, true);

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
					if (!globals.Way.disableHistory) {
						history.pushState({ way: true }, "", url);
					}

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
		if (!Array.isArray(way)) {
			return;
		}

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

				case "submit": {
					executeSubmit(obj);

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

		for (const el of newFrames) {
			const data = frame(el);

			if (data) {
				newNameMap.set(data.name, el);
			}
		}

		const frames = document.querySelectorAll("[data-frame]");

		const replaced = new Set();

		outer: for (const elem of frames) {
			const data = frame(elem);

			if (!data) {
				continue;
			}

			for (const parent of replaced) {
				if (parent.contains(elem)) {
					continue outer;
				}
			}

			const name = data.name;

			const maybe = newNameMap.get(name);

			if (!maybe) {
				// this frame does not exist in the new data
				elem.remove();

				continue;
			}

			const newElem = maybe;

			const oldValue = data.value;

			const newData = frame(newElem);

			const newValue = newData.value;

			if (oldValue !== newValue) {
				replaced.add(elem);

				elem.replaceWith(newElem);
			}
		}
	}

	function executeLocation(location) {
		const xhr = createXhr("GET", location);

		xhr.send();
	}

	function executeSubmit(obj) {
		const id = obj.id;

		if (!id) {
			return;
		}

		const el = document.getElementById(id);

		if (!el) {
			return;
		}

		if (!(el instanceof HTMLFormElement)) {
			return;
		}

		el.dispatchEvent(new Event("submit", { bubbles: true }));
	}

	function frame(el) {
		const dataset = el.dataset;

		if (!dataset) {
			return null;
		}

		const frame = dataset.frame;

		const colon = frame.indexOf(":");

		if (colon === -1) {
			return { name: frame, value: null };
		}

		const name = frame.substring(0, colon);

		const value = frame.substring(colon + 1);

		return { name: name, value: value };
	}

	function domLoaded() {
		const body = document.body;

		body.addEventListener("click", onClick);
		body.addEventListener("input", onInput);
		body.addEventListener("submit", submitListener);
	}

	window.addEventListener("DOMContentLoaded", domLoaded);

})(this);