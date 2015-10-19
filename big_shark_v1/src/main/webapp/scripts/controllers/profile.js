'use strict';

angular.module(COFIG_APP_NAME)
  .controller('ProfileController', function ($scope) {
    $scope.genders = ["男","女"];

  	$scope.form1 = {
  		name: "",
  		gender: "",
      avatar_url: ""
    }
    $scope.message1 = "";

  	$scope.update = function () {
  		if ($scope.form1.name == "") {
        $scope.message1 = "姓名不能为空";
        return;
      }
    }

    $scope.form2 = {
      email: "",
      old_pwd: "",
      new_pwd: "",
      confirm_pwd: ""
    }
    $scope.message2 = "";
    $scope.changePwd = function () {
      if ($scope.form2.name == "") {
        $scope.message1 = "姓名不能为空";
        return;
      }
    }

  });

