 
describe("SimpleService", function(){

    beforeEach(module("app"));

    var service;

    beforeEach(inject(function(SimpleService){
       service = SimpleService;
    }));

    describe("getData", function(){
        it("should return an array of items", function(){
            expect(service.getData()).toBeDefined();
        });
    });
});