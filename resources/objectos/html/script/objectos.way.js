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
		if (executeEvent(event, "onClick")) {
			return;
		}

		const target = event.target;

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
		executeEvent(event, "onInput");
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

	function executeEvent(event, name) {
		let target = event.target;

		while (target instanceof HTMLElement) {
			const dataset = target.dataset;

			const data = dataset[name];

			if (data) {
				const way = JSON.parse(data);

				executeActions(way, target);

				return true;
			} else {
				target = target.parentNode;
			}
		}

		return false;
	}

	function executeActions(actions, element) {
		if (!Array.isArray(actions)) {
			return;
		}

		for (const action of actions) {

			const cmd = action.cmd;

			if (!cmd) {
				continue;
			}

			switch (cmd) {
				case "delay": {
					executeDelay(action, element);

					break;
				}

				case "html": {
					executeHtml(action.value);

					break;
				}

				case "replace-class": {
					executeReplaceClass(action);

					break;
				}

				case "submit": {
					executeSubmit(action);

					break;
				}
			}

		}
	}

	function executeDelay(obj, el) {
		const ms = obj.ms;

		if (!ms) {
			console.error("delay: missing 'ms' property");

			return;
		}

		if (!Number.isInteger(ms)) {
			console.error("delay: invalid 'ms' property. Expected integer but found %s", ms);

			return;
		}

		const actions = obj.actions;

		if (!actions) {
			console.error("delay: missing 'actions' property");

			return;
		}

		if (!Array.isArray(actions)) {
			console.error("delay: invalid 'actions' property. Expected Array but found %s", actions);

			return;
		}

		const dataset = el.dataset;

		let timer = dataset.timer;

		if (!timer) {
			timer = 0;
		}

		const delay = function() {
			clearTimeout(timer);

			timer = setTimeout(() => executeActions(actions, el), ms);

			dataset.timer = timer;
		}

		delay();
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

			if (oldValue !== newValue || oldValue === null && newValue === null) {
				replaced.add(elem);

				elem.replaceWith(newElem);
			}
		}
	}

	function executeLocation(location) {
		const xhr = createXhr("GET", location);

		xhr.send();
	}

	function executeReplaceClass(action) {
		const args = action.args;

		if (!args) {
			return;
		}

		if (args.length !== 3) {
			return;
		}

		const id = args[0];

		const el = document.getElementById(id);

		if (!el) {
			return;
		}

		const classList = el.classList;

		const classA = args[1];

		const classB = args[2];

		classList.replace(classA, classB);
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