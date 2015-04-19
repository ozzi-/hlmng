describe('app: hlmngApp', function() {
	
	beforeEach(module('hlmngApp'));
	  
	
	describe('factory: RestService', function() {
	var factory = null;
		beforeEach(inject(function(MyFactory) {
			factory = MyFactory;
		}));
	
		it('Should define methods', function() {
			expect(factory.list("speaker")).not.toBeNull();
		});
	});
});