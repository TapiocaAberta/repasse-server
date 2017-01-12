(function() {
    'use strict';

    angular.module('repasse')
        .config(function($stateProvider) {
            $stateProvider
                .state('compare', {
                    url: '/compare',
                    templateUrl: 'app/analyse/compare/compare.html',
                    controller: 'CompareCtrl'
                });
        });

})();