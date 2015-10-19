'use strict';
/**
 * @ngdoc overview
 * @name app
 * @description
 * # app
 *
 * Main module of the application.
 */
var COFIG_APP_NAME = 'bigshark';
//var HOST_URL = "http://cdug.tongji.edu.cn:8080";
var HOST_URL = "http://localhost:8080";
angular
  .module(COFIG_APP_NAME, [
    'ngAnimate',
    'ngAria',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ngStorage',
    'ui.router',
    'ui.sortable',
    'ui.bootstrap',
    'oitozero.ngSweetAlert',
    'angularFileUpload',
    'dialogs.main',
    'ui.bootstrap.datepicker',
    'selectize',
    // 'ngFileUpload',
    'datatables',
    "oc.lazyLoad"
  ]).run(function(){
  });
