/**
 * Created by joshoy on 15/8/14.
 */

'use strict';


angular.module(COFIG_APP_NAME)
  .controller('ReviewController',
  function($scope, $localStorage, $window, sessionService, reviewFactory) {

    $localStorage.viewingTabIndex = $localStorage.viewingTabIndex || 0;
    $scope.viewingTab = $localStorage.viewingTabIndex;

    $scope.investTypeMap = {
      'ANGEL': '天使投资',
      'SEED': '种子孵化'
    };

    $scope.changeViewingTab = function(tagIndex) {
      $localStorage.viewingTabIndex = tagIndex;
      $scope.viewingTab = tagIndex;
    };

    /* Lists */

    $scope.auditionProjectList = [
    ];

    $scope.firstReviewProjectList = [
    ];

    $scope.finalReviewProjectList = [
    ];

    $scope.formalProjectList = [
    ];

    $scope.observingProjectList = [
    ];

    $scope.trashProjectList = [
    ];


    $scope.refreshReviewLists = function() {
      reviewFactory.searchReviewLists(sessionService.headers()).get(
        {
          // phase: 'AUDITION'
        },
        function success(data) {
          console.log('获取审查列表成功!');
          console.log(data.data.data);

          var projectList = data.data.data;

          for (var i = 0; i < projectList.length; i++) {
            var project = projectList[i];
            //var d = new Date(project.startDate);
            //console.log(d.toLocaleDateString());
            //var year = d.toLocaleDateString().split('/')[0];
            //var month = d.toLocaleDateString().split('/')[1];

            switch(project.phase) {
              case 'DRAFT':
                break;
              case 'AUDITION':
                $scope.auditionProjectList = $scope.auditionProjectList.concat({
                  pid: project.id,
                  name: project.name,
                  city: project.city,
                  contactName: project.contactName,
                  startTime: project.startDate
                });
                break;
              case 'FIRST_REVIEW':
                $scope.firstReviewProjectList = $scope.firstReviewProjectList.concat({
                  pid: project.id,
                  name: project.name,
                  city: project.city,
                  contactName: project.contactName,
                  startTime: project.startDate,
                  notified: project.notified
                });
                break;
              case 'FINAL_REVIEW':
                $scope.finalReviewProjectList = $scope.finalReviewProjectList.concat({
                  pid: project.id,
                  name: project.name,
                  city: project.city,
                  contactName: project.contactName,
                  startTime: project.startDate,
                  notified: project.notified,
                  investType: project.investType
                });
                break;
              case 'ACCEPTED':
                $scope.formalProjectList = $scope.formalProjectList.concat({
                  pid: project.id,
                  name: project.name,
                  city: project.city,
                  contactName: project.contactName,
                  startTime: project.startDate,
                  investType: project.investType
                });
                break;
              default:
                break;
            }

            // $window.g.dataTableInit();
          }

        },
        function error(data) {
          console.log('获取审查列表失败...');
        }
      );
    };

    $scope.searchReviewLists = function() {

    };


    /* 进入页面后 */
    $scope.refreshReviewLists();
  });
