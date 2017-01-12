(function() {
    'use strict';

    angular.module('repasse')
        .config(function($stateProvider) {
            $stateProvider
                .state('explore', {
                    url: '/explore',
                    templateUrl: 'app/analyse/explore/explore.html',
                    controller: 'ExploreCtrl'
                });
        });

})();