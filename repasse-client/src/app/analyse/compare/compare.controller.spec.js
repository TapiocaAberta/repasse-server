(function() {
    'use strict';

    describe('Controller: CompareCtrl', function() {

        // load the controller's module
        beforeEach(module('repasse'));

        var CompareCtrl, scope;

        // Initialize the controller and a mock scope
        beforeEach(inject(function($controller, $rootScope) {
            scope = $rootScope.$new();
            CompareCtrl = $controller('CompareCtrl', {
                $scope: scope
            });
        }));

        it('should ...', function() {
            expect(1).toEqual(1);
        });
    });

})();