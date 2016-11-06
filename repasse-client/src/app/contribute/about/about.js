(function() {
    'use strict';

    angular.module('repasse')
        .config(function($stateProvider) {
            $stateProvider
                .state('about', {
                    url: '/about',
                    templateUrl: 'app/contribute/about/about.html',
                    controller: 'AboutCtrl'
                });
        });

})();