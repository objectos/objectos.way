/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

	function clickListener(event) {
		let target = event.target;

		while (target instanceof Node) {
			const element = target;

			target = target.parentNode;

			const result = executeEvent(element, "onClick");

			if (result) {
				event.preventDefault();

				break;
			}

			// there was no data-on-click action...
			// but we explicitly stop at these elements

			if (element instanceof HTMLAnchorElement ||
				element instanceof HTMLButtonElement) {
				break;
			}
		}
	}

	function onInput(event) {
		const target = event.target;

		executeEvent(target, "onInput");
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

		xhr.setRequestHeader("Way-Request", "true");

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

	function executeEvent(element, name) {
		const dataset = element.dataset;

		if (!dataset) {
			return false;
		}

		const data = dataset[name];

		if (!data) {
			return false;
		}

		const way = JSON.parse(data);

		return executeActions(way, element);
	}

	function executeActions(actions, element) {
		if (!Array.isArray(actions)) {
			return false;
		}

		if (actions.length === 0) {
			return false;
		}

		let count = 0;

		for (const action of actions) {

			const cmd = action.cmd;

			if (!cmd) {
				continue;
			}

			count++;

			switch (cmd) {
				case "add-class": {
					executeAddClass(action);

					break;
				}

				case "delay": {
					executeDelay(action, element);

					break;
				}

				case "html": {
					executeHtml(action.value);

					break;
				}

				case "location": {
					executeLocation(action.value);

					break;
				}

				case "navigate": {
					executeNavigate(element);

					break;
				}

				case "remove-class": {
					executeRemoveClass(action);

					break;
				}

				case "replace-class": {
					executeReplaceClass(action);

					break;
				}

				case "set-property": {
					executeSetProperty(action);

					break;
				}

				case "stop-propagation": {
					// noop

					break;
				}

				case "submit": {
					executeSubmit(action);

					break;
				}

				case "toggle-class": {
					executeToggleClass(action);

					break;
				}

				default: {
					count--;

					break;
				}
			}

		}

		return count > 0;
	}

	function executeAddClass(action) {
		withElementClassList(action, "add");
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

		if (el) {

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

			return;

		}

		setTimeout(() => executeActions(actions), ms);
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

		window.scrollTo(0, 0);
	}

	function executeLocation(location) {
		const xhr = createXhr("GET", location);

		xhr.send();
	}

	function executeNavigate(element) {
		if (!(element instanceof HTMLAnchorElement)) {
			const name = element.constructor ? element.constructor.name : "Unknown";

			console.error("executeNavigate: expected HTMLAnchorElement but got %s", name);

			return;
		}

		const href = element.href;

		if (!href) {
			console.error("executeNavigate: anchor has no href attribute");

			return;
		}

		executeLocation(href);
	}

	function executeRemoveClass(action) {
		withElementClassList(action, "remove");
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

	function executeSetProperty(action) {
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

		const propertyName = args[1];

		const propertyValue = args[2];

		el.style.setProperty(propertyName, propertyValue);
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

	function executeToggleClass(action) {
		withElementClassList(action, "toggle");
	}

	function withElementClassList(action, methodName) {
		const args = action.args;

		if (!args) {
			return;
		}

		if (args.length < 2) {
			return;
		}

		const id = args[0];

		const el = document.getElementById(id);

		if (!el) {
			return;
		}

		const classList = el.classList;

		for (let idx = 1; idx < args.length; idx++) {
			const className = args[idx];

			classList[methodName](className);
		}
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

		body.addEventListener("click", clickListener);
		body.addEventListener("input", onInput);
		body.addEventListener("submit", submitListener);
	}

	window.addEventListener("DOMContentLoaded", domLoaded);

})(this);