(function() {
    'use strict';

    describe('Controller: ExploreCtrl', function() {

        // load the controller's module
        beforeEach(module('repasse'));

        var ExploreCtrl, scope;

        // Initialize the controller and a mock scope
        beforeEach(inject(function($controller, $rootScope) {
            scope = $rootScope.$new();
            ExploreCtrl = $controller('ExploreCtrl', {
                $scope: scope
            });
        }));

        it('should ...', function() {
            expect(1).toEqual(1);
        });
    });

})();