// based on https://github.com/bigskysoftware/idiomorph/blob/8e40c42cc573609eb6863e72fa3403574974dd7d/test/test-utilities.js

function make(htmlStr) {
	const range = document.createRange();

	const fragment = range.createContextualFragment(htmlStr);

	const children = fragment.children;

	const element = children[0];
	
	const wa = getWorkArea();
	
	wa.appendChild(element);

	return element;
}

function makeServer() {
	const server = sinon.fakeServer.create();

	server.fakeHTTPMethods = true;

	return server;
}

function getWorkArea() {
	return document.getElementById("work-area");
}

function clearWorkArea() {
	getWorkArea().innerHTML = "";
}

function print(elt) {
	let text = document.createTextNode(elt.outerHTML + "\n\n");
	getWorkArea().appendChild(text);
	return elt;
}