suite("Action::delay test", function() {

	setup(function() {
		clearWorkArea();
		this.server = makeServer();
	});

	teardown(function() {
		this.server.restore();
	});

	test("it should execute the data-on-click action", function() {
		const clickMe = "click-me";

		const addClass = JSON.stringify([{
			cmd: "delay", ms: 200, actions: [
				{ cmd: "add-class", args: ["subject", "bar"] }
			]
		}]);

		make(`
		<div id='${clickMe}' data-on-click='${addClass}'>
		<div id='subject' class='foo'></div>
		</div>
		`);

		byId(clickMe).click();

		setTimeout(() => {
			const subject = byId("subject");

			const l = subject.classList;

			assert.equal(l.length, 2);
			assert.isTrue(l.contains("foo"));
			assert.isTrue(l.contains("bar"));
		}, 300);
	});

	test("it should execute the POST response action", function() {
		const clickMe = "click-me";

		make(`
		<form method='post' action='/test'>
		<button id='${clickMe}' type='submit'>Click Me</button>
		<div id='subject' class='foo'></div>
		</form>
		`);

		this.server.respondWith(
			"POST", "/test", [
			200,
			{ "Content-Type": "application/json" },
			JSON.stringify([{
				cmd: "delay", ms: 200, actions: [
					{ cmd: "add-class", args: ["subject", "bar"] }
				]
			}])
		]);

		byId(clickMe).click();

		this.server.respond();

		setTimeout(() => {
			const subject = byId("subject");

			const l = subject.classList;

			assert.equal(l.length, 2);
			assert.isTrue(l.contains("foo"));
			assert.isTrue(l.contains("bar"));
		}, 300);
	});

});