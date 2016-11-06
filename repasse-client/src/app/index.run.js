(function() {
    'use strict';

    angular
        .module('repasse')
        .run(runBlock);

    /** @ngInject */
    function runBlock($log) {

        $log.debug('runBlock end');
    }

})();