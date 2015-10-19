'use strict';

angular.module(COFIG_APP_NAME)
  .directive('labFooter', function () {
    return {
      templateUrl: 'templates/footer.html',
      restrict: 'E',
      controller: function ($scope, $rootScope, $location) {
        
      },
      link: function postLink(scope, element, attrs) {

      }
    };
  });
