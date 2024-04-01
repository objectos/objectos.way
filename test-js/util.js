// based on https://github.com/bigskysoftware/idiomorph/blob/8e40c42cc573609eb6863e72fa3403574974dd7d/test/test-utilities.js
// based on https://github.com/bigskysoftware/htmx/blob/c247cae9bf04b5b274d3bd65937541e8224a359c/test/util/util.js

function byId(id) {
	return document.getElementById(id);
}

function innerHTML(id) {
	const element = document.getElementById(id);

	return element.innerHTML;
}

function make(html) {
	const element = makeElement(html);

	const wa = workArea();

	wa.appendChild(element);

	return element;
}

function makeElement(html) {
	const range = document.createRange();

	const fragment = range.createContextualFragment(html);

	const children = fragment.children;

	return children[0];
}

function makeServer() {
	const server = sinon.fakeServer.create();

	server.fakeHTTPMethods = true;

	return server;
}

function workArea() {
	return document.getElementById("work-area");
}

function clearWorkArea() {
	workArea().innerHTML = "";
}

function print(elt) {
	let text = document.createTextNode(elt.outerHTML + "\n\n");

	workArea().appendChild(text);

	return elt;
}

function queryString(url) {
	const question = url.indexOf("?");

	if (question == -1) {
		return "";
	}

	const hash = url.indexOf("#");

	if (hash == -1) {
		return url.substring(question + 1);
	}

	return url.substring(question + 1, hash);
}