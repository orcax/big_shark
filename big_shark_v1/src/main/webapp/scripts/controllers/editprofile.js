"use strict";

/*Created by JoshOY*/

angular.module(COFIG_APP_NAME)
  .controller('EditProfileController', function($scope, $localStorage, qService, $location,
                                                $upload, sessionService, accountFactory) {
    $scope.genderOptions = ['男', '女'];

    $scope.realName = $localStorage.currentUser.name;
    $scope.realNameEdit = $localStorage.currentUser.name;
    $scope.contactMail = $localStorage.currentUser.email;
    $scope.avatarUrl = '';

    $scope.oldPassword = '';
    $scope.newPassword = '';
    $scope.newPasswordRepeat = '';

    $scope.gender = ($localStorage.currentUser.gender === 'MALE') ? '男' : '女';

    $scope.saveUserInfo = function() {
      accountFactory.saveUserInfo(sessionService.headers()).put(
        {
          gender: ($scope.gender === '男') ? 'MALE' : 'FEMALE',
          name: $scope.realNameEdit
        },
        function success(data) {
          alert('个人信息更新成功!');
          console.log(data);
          $scope.realName = $scope.realNameEdit;
          $localStorage.currentUser.name = $scope.realNameEdit;
          $localStorage.currentUser.gender = ($scope.gender === '男') ? 'MALE' : 'FEMALE';
        },
        function error(data) {
          console.log('更新个人信息失败...错误原因: ');
          console.log(data);
        }
      );
    };

    $scope.updateUserAvatar = function(files) {
      if (files && files.length) {
        for (var i = 0; i < files.length; i++) {
          var file = files[i];
          console.log('将要上传的头像: ');
          console.log(file);
          // 上传头像
          $upload.upload({
            url: HOST_URL + '/api/account/icon',
            headers: sessionService.headers(),
            file: file,
            fileFormDataName: 'icon'
          }).progress(
            function(evt) {
              console.log('上传进度: ' + parseInt(100.0 * evt.loaded / evt.total));
            }
          ).success(
            function(data, status, headers, config) {
              console.log('上传新头像成功! ');

            }
          );
        }
      }
    };

    $scope.updateNewPassword = function() {
      if ((!$scope.oldPassword) || (!$scope.newPassword)) {
        alert('密码不能为空!');
        return;
      }
      if ($scope.newPassword !== $scope.newPasswordRepeat) {
        alert('新密码重复不一致!');
        return;
      }
      var header = sessionService.headers();
      header['Content-Type'] = 'application/x-www-form-urlencoded';

      accountFactory.updateNewPassword(header).put(
        {
          oldPassword: $scope.oldPassword,
          newPassword: $scope.newPassword
        },
        function success() {
          alert('密码修改成功!');
        },
        function error(data) {
          alert('哎呀, 出错了...检查一下网络看看?');
          console.log(data);
        }
      );
    };

  });
