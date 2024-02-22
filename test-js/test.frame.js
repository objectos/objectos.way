suite("Frame test", function() {

	setup(function() {
		clearWorkArea();
		this.server = makeServer();
	});

	teardown(function() {
		this.server.restore();
	});

	test("frame contents should be replaced", function() {
		make("<form id='form' data-frame='form' data-frame-value='one' method='post' action='/test'><button id='btn' type='submit'>Before</button></form>");

		this.server.respondWith(
			"POST",
			"/test", [
			200,
			{ "Content-Type": "application/json" },
			JSON.stringify([
				{
					cmd: "html",
					value: "<form id='form' data-frame='form' data-frame-value='two' method='post' action='/test'><button id='btn' type='submit'>After</button></form>"
				},
				{ cmd: "replace", id: "form" }
			])
		]);

		let btn = document.getElementById("btn");

		assert.equal(btn.innerHTML, "Before");

		btn.click();

		this.server.respond();

		btn = document.getElementById("btn");

		assert.equal(btn.innerHTML, "After");
	});

});