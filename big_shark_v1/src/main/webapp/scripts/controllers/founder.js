/**
 * Created by joshoy on 15/9/1.
 */

'use strict';

angular.module(COFIG_APP_NAME)
  .controller('FounderController', function($scope) {

    $scope.founderName = '创始人姓名';
    $scope.founderJob = '创始人职位';

    $scope.founderCellphone = '15221529016';
    $scope.founderCitizenId = '360104199411041514';
    $scope.founderWechatId = '15221529016';
    $scope.founderEmail = 'JoshOY789@gmail.com';

    $scope.founderExperiences = [
      {
        'corporation': '<所在公司A>',
        'job': '<担任职位A>',
        'time': '2010.1.1 ~ 2012.1.1'
      },
      {
        'corporation': '<所在公司B>',
        'job': '<担任职位B>',
        'time': '2012.1.2 ~ 2014.1.1'
      }
    ];

    $scope.educationInfo = [
      {
        'schoolName': '<学校名称A>',
        'degree': '<学位A>',
        'time': '2000.9.1 ~ 2004.6.1'
      },
      {
        'schoolName': '<学校名称B>',
        'degree': '<学位B>',
        'time': '2004.9.1 ~ 2008.6.1'
      }
    ];

  });
