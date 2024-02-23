suite("Frame test", function() {

	setup(function() {
		clearWorkArea();
		this.server = makeServer();
	});

	teardown(function() {
		this.server.restore();
	});

	test("single data-frame: same name different value", function() {
		make("<form data-frame='x' data-frame-value='1' method='post' action='/test'><button id='b' type='submit'>Before</button></form>");
		const frame2 = makeElement("<form data-frame='x' data-frame-value='2' method='post' action='/test'><button type='submit'>After</button></form>");

		this.server.respondWith(
			"POST",
			"/test", [
			200,
			{ "Content-Type": "application/json" },
			JSON.stringify([
				{
					cmd: "html",
					value: frame2.outerHTML
				}
			])
		]);

		const btn = byId("b");

		btn.click();

		this.server.respond();

		assert.equal(workArea().innerHTML, frame2.outerHTML);
	});

});