(function() {
    'use strict';

    angular.module('repasse')
        .config(function($stateProvider) {
            $stateProvider
                .state('developers', {
                    url: '/developers',
                    templateUrl: 'app/contribute/developers/developers.html',
                    controller: 'DevelopersCtrl'
                });
        });

})();