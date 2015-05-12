describe("app module", function () {
    beforeEach(module("hlmngApp"));

    describe("EventListController", function () {
        var scope,
            controller;

        beforeEach(inject(function ($rootScope, $controller) {
            scope = $rootScope.$new();
            controller = $controller;
        }));

        it("should load events", function () {
            controller("EventListController", {$scope: scope});
            expect(scope.events).not.toBeNull();
        });
    });
});  
