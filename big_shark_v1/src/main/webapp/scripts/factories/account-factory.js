'use strict';

angular.module(COFIG_APP_NAME)
  .factory('accountFactory', function ($resource, $rootScope) {
    var baseUrl = HOST_URL + '/api/account';
    var headers = {'x-auth-token': $rootScope.token};

    return {
      signup: function(){
        return $resource(baseUrl + '/customer', null,{
          'post':{
            method: 'POST'
          }
        });
      },
      profile: function(h){
        var headers = h || headers;
        //获取用户的 profile 接口
        return $resource(baseUrl + '/me/profile', null, {
          'get': {
            method : 'GET',
            headers : headers
          },
          'put': {
            method: 'PUT',
            headers: headers
          }

        });
      },
      adminUpdateUserProfile : function(data){
        //@ADMINISTRATOR Required
        //管理员更新用户profile
        return $resource(baseUrl + '/profile/:id',{id:'@accountId'},
        {
          'put': {
            method: 'PUT',
            headers: headers
          },
          'get': {
            method: 'GET',
            headers: headers
          }
        });

      },

      /* 发送手机验证码用 */
      sendCaptcha: function(header) {
        return $resource(HOST_URL + '/api/account/captcha',
          {leaderMobile: '@leaderMobile'},
          {
            'post': {
              method: 'POST',
              headers: header
            }
          }
        );
      },

      /* 验证手机验证码 */
      verifyCaptcha: function(header) {
        return $resource(HOST_URL + '/api/account/captcha/verify',
          {
            leaderMobile: '@leaderMobile',
            code:'@code'
          },
          {
            'put': {
              method: 'PUT',
              headers: header
            }
          }
        )
      },

      /* 更新用户基本信息 */
      saveUserInfo: function(header) {
        return $resource(HOST_URL + '/api/account/me/profile',
          {
            gender: '@gender',
            name: '@name',
            mobile: '@mobile',
            email: '@email'
          },
          {
            'put': {
              method: 'PUT',
              headers: header
            }
          }
        );
      },

      /* 更新用户密码 */
      updateNewPassword: function(header) {
        return $resource(HOST_URL + '/api/account/me/password',
          {
            oldPassword: '@oldPassword',
            newPassword: '@newPassword'
          },
          {
            'put': {
              method: 'PUT',
              headers: header
            }
          }
        );
      },

      /* 新增投资经理 */
      newManager: function(header) {
        return $resource(HOST_URL + '/api/account/manager',
          {
            'email': '@email',
            'icon': '@icon',
            'password': '@password'
          },
          {
            'post': {
              method: 'POST',
              headers: header
            }
          });
      },

      /* 获取普通用户账户列表 */
      getCustomersList: function(header) {
        return $resource(HOST_URL + '/api/account/manager/list?userType=CUSTOMER',
          {},
          {
            'get': {
              method: 'GET',
              headers: header
            }
          });
      },

      /* 管理员获取投资经理列表 */
      getManagersList: function(header) {
        return $resource(HOST_URL + '/api/account/manager/list?userType=MANAGER',
          {},
          {
            'get': {
              method: 'GET',
              headers: header
            }
          });
      }

    };
  });
