/*
 *  Altair Admin angularjs
 *  controller
 */

angular
    .module('altairApp')
    .controller('mainCtrl', ['$rootScope',
        '$scope',
        '$timeout',
        function ($rootScope,$scope,$timeout) {
            $rootScope.fullHeaderActive = true;
            
            $scope.$on('$destroy', function() {
                $rootScope.fullHeaderActive = false;
            });
        }
    ])
;
