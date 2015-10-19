'use strict';

angular.module(COFIG_APP_NAME)
  .directive('labHeader', function () {
    return {
      templateUrl: 'templates/header.html',
      restrict: 'E',
      controller: function ($scope, $rootScope, sessionService,
                            tokenFactory, accountFactory, $localStorage, $location) {
        $scope.currentUser = $localStorage.currentUser;
        $scope.currentToken = $localStorage.token;
        $scope.isLogin = ($scope.currentUser && $scope.currentToken) ? true : false;
        // console.log($scope.currentUser.role);

        if ($scope.isLogin) {
          console.log($scope.currentUser);
          // console.log('当前用户: %s\n当前Token: %s\n', $scope.currentUser.account, $scope.currentToken);
        }
        if (!$scope.isLogin) {
          console.log('当前状态为未登录状态...');
        }

        /* 退出登录用到的ng-click方法 */
        $scope.logout = function() {
          // sessionService.delToken();
          console.log('登出中...');
          if ($scope.isLogin === false) {
            console.log('退出登录成功!');
            $scope.isLogin = false;
            $location.path('/main');
            return;
          }
          tokenFactory.logout(sessionService.headers())
          .delete(
            {},
            function success(data, headers) {
              console.log('退出登录成功!');
              sessionService.delToken();
              $scope.isLogin = false;
              $location.path('/main');
            },
            function error(data) {
              sessionService.delToken();
              $scope.isLogin = false;
              $location.path('/main');
              console.log('退出登录失败!');
            }
          );
        };

        /* 验证当前登录的Token是否还有效 */
        if ($scope.isLogin) {
          accountFactory.profile(sessionService.headers()).get(
            {},
            function success(data) {
              console.log('登录状态确认...');
              return null;
            },
            function error(data) {
              console.log('Token已过期, 需要重新登录.');
              $scope.logout();
            }
          );
        }

      },
      link: function postLink(scope, element, attrs) {

      }
    };
  });
