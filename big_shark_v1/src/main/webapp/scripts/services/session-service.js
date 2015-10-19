'use strict';

angular.module(COFIG_APP_NAME).service('sessionService',
	function categoryService ($localStorage, $location,
		$rootScope, tokenFactory, qService, $route) {

		function gCurrSemester(){
			if($localStorage.semester){
				$rootScope.semester = $localStorage.semester;
				return $localStorage.semester;
			}else{
				return null;
			}
		};
		function checkLocalToken(){
			if (!$localStorage.token  || !$localStorage.currentUser) {
				$location.path('/login');
				return false;
			}else{
				$rootScope.currentUser = $localStorage.currentUser;
				$rootScope.token = $localStorage.token;
			}
		};

		this.checkToken = function() {
			return checkLocalToken();
		};

		this.getCurrSemeter = function(){
			return gCurrSemester();
		};

		this.storageChecking = function(){
			 checkLocalToken();
			 checkLocalToken();
		};

		this.saveCurrSemeter = function(semester){
			$localStorage.semester = semester;
			$rootScope.semester = semester;
		};

		this.saveToken = function(user, token) {
			var user = user;
			$localStorage.currentUser = user;
			$localStorage.token = token;
			$rootScope.currentUser = user;
			$rootScope.token = token;
		};

		this.delToken = function() {
			delete $localStorage.currentUser;
			delete $localStorage.token;
			delete $rootScope.currentUser;
			delete $rootScope.token;
			delete $localStorage.semester;
			delete $rootScope.semester;
			// $location.path('/login');
		};

		this.headers = function(){
			return {
      	'x-auth-token': $localStorage.token
    	};
		};
	}
);
