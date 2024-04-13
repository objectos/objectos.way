suite("data-on-click test", function() {

	setup(function() {
		clearWorkArea();
		this.server = makeServer();
	});

	teardown(function() {
		this.server.restore();
	});

	test("submit", function() {
		const clickId = "f";
		const onClick = JSON.stringify([{
			cmd: "replace-class", 
			args: ["subject", "a", "b"]
		}]);
		make(`
		<div id='${clickId}' data-on-click='${onClick}'>
		<div id='subject' class='a x'></div>
		</div>		
		`);

		byId(clickId).click();
		
		const subject = byId("subject");

		const l = subject.classList;

		assert.equal(l.length, 2);
		assert.isFalse(l.contains("a"));
		assert.isTrue(l.contains("b"));
		assert.isTrue(l.contains("x"));
	});
	
});