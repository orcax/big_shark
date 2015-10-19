'use strict';

angular.module(COFIG_APP_NAME).factory('tokenFactory', function($resource) {
	this.login = function(headers) {
		return $resource(HOST_URL + '/api/token', null, {
			'post': {
				method: 'POST',
				headers: headers
			}
		});
	};
	this.isLogin = function(headers) {
		return $resource(HOST_URL +'/api/token/isLogin', null, {
			'get': {
				method: 'GET',
				headers: headers
			}
		});
	};

  /* 登出的factory方法 */
  this.logout = function(headers) {
    return $resource(HOST_URL + '/api/token/', null, {
      'delete': {
        method: 'DELETE',
        headers: headers
      }
    })
  };



  return this;
});
