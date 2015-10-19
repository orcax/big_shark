'use strict';

angular.module(COFIG_APP_NAME)
  .controller('LoginController', function($scope, $localStorage, $http,
    sessionService, tokenFactory, modalService, qService, $location) {
    $scope.login_name = "";
    $scope.login_password = "";
    $scope.submitted = false;

    $scope.login = function() {
      $scope.message = "";
      var _n = $scope.login_name;
      var _p = $scope.login_password;

      console.log('login.....');

      if ($scope.loginForm.$valid) {
        //submit as norml
        tokenFactory.login({
          'X-Username': _n,
          'X-Password': _p
        }).post({},
          function success(data, headers) {
            console.log('登录成功');
            sessionService.saveToken(data.data, headers()['x-auth-token']);
            $location.path('/main');
          },
          function error(data) {
            $scope.message = "用户名或密码错误";
            //modalService.signleConfirmInform('登录失败','用户名或密码错误','warning',function(){});
          });
      } else {
        $scope.loginForm.submitted = true;
      };

    };

    $scope.forgotPassword = function() {
      //modalService.signleConfirmInform('请联系管理员','联系电话: 021-65982267 力学实验中心','info', function(){});
    };
  });


angular.module(COFIG_APP_NAME)
  .controller('SignupController', function($scope, accountFactory, qService) {
    //这里需要修改成 angular的方式，多以对象存储
    $scope.genders = [{value:'MALE',text:'男'},{value:'FEMALE',text:'女'}];
    $scope.submitted = false;
    $scope.singupData = { //angular对象推荐使用对象存储
      account: "",
      password: "",
      name : "",
      gender: ""
    };
    $scope.message = "";

    $scope.signup = function() {
      if ($scope.signupForm.$valid) {
        console.log($scope.signupData);
        $scope.signupData.gender = $scope.signupData.gender.value;
        var promise = qService.tokenHttpPost(accountFactory.signup, null, $scope.signupData);
        promise.then(function(result){
          //基于promise处理,错误结果已经在qService里面统一封装
          console.log(result);
        });
      } else {
        $scope.signupForm.submitted = true;
      };
    };
  });

 angular.module(COFIG_APP_NAME)
   .controller('ManagerLoginController',
   function($scope) {

     $scope.inputUsername = '';
     $scope.inputPassword = '';



   });
