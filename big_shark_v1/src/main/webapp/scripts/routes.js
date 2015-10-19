'use strict';

var app = angular.module(COFIG_APP_NAME)
  .config(['$stateProvider','$urlRouterProvider','$locationProvider',
    function($stateProvider,$urlRouterProvider,$locationProvider){
    $locationProvider.html5Mode({
      enabled: false,
      requireBase: false
    });
    $urlRouterProvider
          .when("","/main")
          .otherwise('/login');
    $stateProvider
        .state('login', {
          url: "/login",
          templateUrl: "views/login.html",
          controller: 'LoginController',
          resolve: {
            deps: ['$ocLazyLoad',
              function($ocLazyLoad){
                return $ocLazyLoad.load('oitozero.ngSweetAlert');
            }]
          }
        })
        .state('signup', {
          url: "/signup",
          templateUrl: "views/signup.html",
          controller: 'SignupController',
          resolve: {
            deps: ['$ocLazyLoad',
              function($ocLazyLoad){
                return $ocLazyLoad.load('oitozero.ngSweetAlert');
            }]
          }
        })
         .state('main', {
          url: "/main",
          templateUrl: "views/main.html",
          controller: 'MainController'
        })
         .state('main.second', {
          url: "/second?p1&p2",
          templateUrl: "views/main.html",
          controller: 'MainController',
          resolve: {
            p1: function($stateParams){
              console.log($stateParams.p1);
              console.log($stateParams.p2);
            }
          }
        })
         .state('projects', {
          url: "/projects",
          templateUrl: "views/projects.html",
          controller: 'ProjectsController'
        })
         .state('createproject', {
          url: "/projects/new",
          templateUrl: "views/createproject.html",
          controller: 'CreateProjectController'
        })
         .state('editproject', {
          url: "/projects/edit",
          templateUrl: "views/editproject.html",
          controller: 'EditProjectController',
          resolve: {
            id: function($stateParams){
              console.log($stateParams.id);
            }
          }
        })
         .state('profile', {
          url: "/profile",
          templateUrl: "views/profile.html",
          controller: 'ProfileController'
        })
          .state('editprofile', {
          url: '/editprofile',
          templateUrl: 'views/editprofile.html',
          controller: 'EditProfileController'
        })
          .state('formalproject', {
          url: '/formalproject',
          templateUrl: 'views/formalproject.html',
          controller: 'formalProjectController'
        })
          .state('review', {
          url: '/review',
          templateUrl: 'views/review.html',
          controller: 'ReviewController'
        }).state('reviewdetail', {
          url: '/reviewdetail/:pid',
          templateUrl: 'views/reviewdetail.html',
          controller: 'ReviewDetailController'
        }).state('managerLogin', {
          url: '/managerlogin',
          templateUrl: 'views/managerLogin.html',
          controller: 'ManagerLoginController'
        }).state('founder', {
          url: '/founder/:founderId',
          templateUrl: 'views/founder.html',
          controller: 'FounderController'
        }).state('formalProject', {
          url: '/formalproject/:pid',
          templateUrl: 'views/formalproject.html',
          controller: 'formalProjectController'
        });
  }]);
