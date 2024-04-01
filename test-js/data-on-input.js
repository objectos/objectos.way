suite("data-on-input test", function() {

	setup(function() {
		clearWorkArea();
		this.server = makeServer();
	});

	teardown(function() {
		this.server.restore();
	});

	test("submit", function() {
		const formId = "f";
		const onInput = JSON.stringify([{
			cmd: "submit", value: formId
		}]);
		make(`
		<div data-frame='x' data-frame-value='foo'>
		<form id='${formId}' action='/list' method='get'>
		<input id='q' type='text' name='q' data-on-input='${onInput}'>
		</form>
		</div>
		`);
		const page2 = makeElement("<div data-frame='x' data-frame-value='bar'><p>the results</p></div>");

		this.server.respondWith("GET", "/list?q=abc", function(xhr) {
			const q = queryString(xhr.url);

			assert.equal(q, "q=abc");

			xhr.respond(200, { "Content-Type": "text/html" }, page2.outerHTML);
		});

		const q = byId("q");

		q.value = "abc";

		q.dispatchEvent(new Event("input", { bubbles: true }));

		this.server.respond();

		assert.equal(workArea().innerHTML, page2.outerHTML);
	});

});