/**
 * Created by joshoy on 15/8/17.
 */

'use strict';

angular.module(COFIG_APP_NAME)
  .controller('ReviewDetailController',
  function($scope, $stateParams, $location, $route,
           reviewFactory, projectFactory, sessionService) {
    // console.log($stateParams);

    /* Selections */
    $scope.opinionSelection = [true, false];
    $scope.angelSeedOption = 'ANGEL';

    /* Maps */
    $scope.projectStatusMap = {
      'audition': '海选中',
      'firstReview': '初审中',
      'finalReview': '终审中',
      'formal': '正式项目'
    };

    $scope.projectStatusMapUpper = {
      'AUDITION': '海选中',
      'FIRST_REVIEW': '初审中',
      'FINAL_REVIEW': '终审中',
      'ACCEPTED': '正式项目'
    };

    $scope.investTypeMap = {
      'A': '天使投资',
      'S': '种子孵化'
    };

    $scope.opinionMap = {
      true: 'YES',
      false: 'NO'
    };

    /* input models */

    $scope.addFirstReviewRemarkModel = {
      managerName: '',
      content: '',
      inclined: true
    };

    $scope.addFinalReviewRemarkModel = {
      managerName: '',
      content: '',
      inclined: true,
      maxInvestment: '',
      highestEvaluation: '',
      leadInvest: false
    };

    /* 测试用 */

    $scope.projectTitle = '载入中...';
    $scope.status = '';
    $scope.startTimeYear = 2015;
    $scope.startTimeMonth = 5;
    $scope.projectCity = '载入中...';
    $scope.projectType = '载入中...';
    $scope.projectDescription = '载入中...';
    $scope.projectAttachments = [
      //{
      //  name: '附件A.pdf',
      //  fid: 1
      //},
      //{
      //  name: '附件B.pdf',
      //  fid: 2
      //}
    ];

    $scope.contactName = '载入中...';
    $scope.contactEmail = '载入中...';
    $scope.contactPhone = '载入中...';

    /* 海选项目 */
    $scope.likeManagerList = [
      //{
      //  uid: 4,
      //  managerName: '经理A'
      //},
      //{
      //  uid: 5,
      //  managerName: '经理B'
      //}
    ];

    /* 初审项目 */
    $scope.firstReviewProjectRemarksList = [
      //{
      //  uid: 4,
      //  managerName: '投资经理A',
      //  content: '这个项目不错，行业方向选的好，用户基数够大，每天都要用！',
      //  like: true
      //},
      //{
      //  uid: 5,
      //  managerName: '投资经理B',
      //  content: '虽然创始人背景很好，但是我觉得团队的地推难度比较大，在短时间内无法有爆发式的增长，需要再考虑考虑',
      //  like: false
      //}
    ];

    /* 终审项目 */

    $scope.finalReviewProjectRemarksList = [
      //{
      //  uid: 4,
      //  managerName: '投资经理A',
      //  content: '这个项目不错，行业方向选的好，用户基数够大，每天都要用！',
      //  like: true,
      //  upInvestment: '1,000,000',
      //  highestValuation: '10,000,000',
      //  investType: 'S',
      //  leadInvestor: true
      //},
      //{
      //  uid: 5,
      //  managerName: '投资经理B',
      //  content: '虽然创始人背景很好，但是我觉得团队的地推难度比较大，在短时间内无法有爆发式的增长，需要再考虑考虑',
      //  like: false,
      //  upInvestment: '1,000,000',
      //  highestValuation: '10,000,000',
      //  investType: 'S',
      //  leadInvestor: false
      //}
    ];

    /* 测试用END */


    /* Initialize functions */
    $scope.initializeFetchData = function() {

      reviewFactory.getReviewDetailById($stateParams.pid, sessionService.headers()).get(
        {},
        function success(data) {
          console.log('项目信息载入成功!');
          console.log(data);

          $scope.status = data.data.phase;

          $scope.projectTitle = data.data.name;
          $scope.projectCity = data.data.city;
          $scope.projectType = data.data.type;
          $scope.projectDescription = data.data.description;

          $scope.contactName = data.data.contactName;
          $scope.contactEmail = data.data.contactEmail;
          $scope.contactPhone = data.data.contactMobile;
        },
        function error() {
          console.log('载入失败 T_T ...');
        }
      );

      projectFactory.getProjectFileList(sessionService.headers(), $stateParams.pid).get(
        {
          // id: $stateParams.pid
        },
        function success(data) {
          console.log('附件信息获取成功!');
          console.log(data);
          $scope.projectAttachments = [];
          var files = data.data;
          for (var i = 0; i < files.length; i++) {
            $scope.projectAttachments = $scope.projectAttachments.concat({
              name: files[i].name,
              fid: parseInt(files[i].id)
            });
          }
        },
        function error() {
          console.log('获取附件信息失败...');
        }
      );

      reviewFactory.getProjectReviewRemarkInfo(sessionService.headers(), $stateParams.pid, 'AUDITION,FIRST_REVIEW,FINAL_REVIEW').get(
        {},
        function success(data) {
          var remark;
          console.log('获取海选项目点评信息成功!');
          console.log(data);
          // $scope.firstReviewProjectRemarksList;
          $scope.firstReviewProjectRemarksList = [];
          for(var i = 0; i < data['data']['FIRST_REVIEW'].length; i++) {
            remark = data['data']['FIRST_REVIEW'][i];
            $scope.firstReviewProjectRemarksList = $scope.firstReviewProjectRemarksList.concat(
              {
                'uid': null,
                'managerName': remark.managerName,
                'like': remark.inclined,
                'content': remark.content
              }
            );
          }
          //console.log('$scope.firstReviewProjectRemarksList: ');
          //console.log($scope.firstReviewProjectRemarksList);
          $scope.finalReviewProjectRemarksList = [];
          for(var j = 0; j < data['data']['FINAL_REVIEW'].length; j++) {
            remark = data['data']['FINAL_REVIEW'][j];
            $scope.finalReviewProjectRemarksList = $scope.finalReviewProjectRemarksList.concat(
              {
                'uid': null,
                'managerName': remark.managerName,
                'content': remark.content,
                'like': remark.inclined,
                'upInvestment': remark.investAmount,
                'highestValuation': remark.assessedValue,
                'investType': 'S',
                'leadInvestor': remark.invested
              }
            );
          }
          console.log($scope.finalReviewProjectRemarksList);
        },
        function error(data) {
          alert('获取海选项目点评信息失败……');
        }
      );

    };

    /* 点赞 */
    $scope.likeThisProject = function() {
      reviewFactory.addReviewInfo($stateParams.pid,
        {
          'liked': '@liked'
        },
        sessionService.headers()).put(
          { 'liked': true },
          function success(data) {
            console.log('点赞成功!');
            console.log(data);
          },
          function error() {
            console.log('点赞失败...');
          }
      );
    };

    /* 初审评审通过 */
    $scope.passFirstReview = function() {
      reviewFactory.addReviewInfo($stateParams.pid,
        {
          'managerName': '@managerName',
          'content': '@content',
          'inclined': '@inclined'
        },
        sessionService.headers()
      ).put(
        {
          'managerName': $scope.addFirstReviewRemarkModel.managerName,
          'content': $scope.addFirstReviewRemarkModel.content,
          'inclined': $scope.addFirstReviewRemarkModel.inclined
        },
        function success(data) {
          console.log('通过初审完成!');
          console.log(data);
        },
        function error() {
          console.log('通过初审失败...');
        }
      );
    };

    /* 终审评审通过 */
    $scope.passFinalReview = function() {
      reviewFactory.addReviewInfo(
        $stateParams.pid,
        {
          "managerName": "@managerName",
          "content": "@content",
          "inclined": '@inclined',
          "assessedValue": "@assessedValue",
          "investAmount": "@investAmount",
          "invested": "@invested"
        },
        sessionService.headers()
      ).put(
        {
          'managerName': $scope.addFinalReviewRemarkModel.managerName,
          'content': $scope.addFinalReviewRemarkModel.content,
          'inclined': $scope.addFinalReviewRemarkModel.inclined,
          'assessedValue': $scope.addFinalReviewRemarkModel.highestEvaluation,
          'investAmount': $scope.addFinalReviewRemarkModel.maxInvestment,
          'invested': $scope.addFinalReviewRemarkModel.leadInvest
        },
        function success(data) {
          console.log('通过终审完成!');
          console.log(data);
        },
        function error() {
          console.log('通过终审失败...');
        }
      );
    };

    /* 正式项目评审通过 */
    $scope.passFormalReview = function() {
      var ok = confirm('确定进入正式项目进行备案吗?');
      if (!ok) {
        return null;
      }
      reviewFactory.changeProjectPhase(sessionService.headers(), $stateParams.pid, 'ACCEPTED').post(
        {},
        function success(data) {
          if (data.code === '200') {
            alert('已成功进入正式项目.');
            $location.path('/formalproject/' + $stateParams.pid);
          }
          else {
            alert('好像出错了: ' + data.message);
          }
        },
        function error() {
          alert('好像网络不太正常, 检查一下网络环境看看?');
        }
      );
    };

    /* 更改项目的所在阶段 */
    $scope.changeProjectPhase = function(phase, investType /* One of 'SEED', 'ANGEL' */) {
      var projectId = $stateParams.pid;
      var invType = investType || '';
      if (phase !== 'FINAL_REVIEW') {
        invType = '';
      }
      reviewFactory.changeProjectPhase(sessionService.headers(), projectId, phase).post(
        {
          investType: invType
        },
        function success(data) {
          alert('已成功调整当前项目的状态至: ' + $scope.projectStatusMapUpper[phase]);
          $location.path('/review');
          $route.reload();
        },
        function error() {
          alert('调整失败...');
        }
      );
    };

    /* 切换天使投资/种子投资 */
    $scope.changeOptionToAS = function(option) {
      $scope.angelSeedOption = option;
      console.log('当前已经设置为: ' + option);
    };

    /* 添加初审评审信息 */
    $scope.addFirstReviewRemarkInfo = function() {
      reviewFactory.newFirstReviewRemark(sessionService.headers()).post(
        {
          'phase': 'FIRST_REVIEW',
          'projectId': $stateParams.pid,
          'managerName': $scope.addFirstReviewRemarkModel.managerName,
          'content': $scope.addFirstReviewRemarkModel.content,
          'inclined': $scope.addFirstReviewRemarkModel.inclined
        },
        function success(data) {
          if(data.code === '200') {
            alert('添加评审内容成功.');
            $scope.firstReviewProjectRemarksList = [
              {
                'uid': null,
                'managerName': $scope.addFirstReviewRemarkModel.managerName,
                'like': $scope.addFirstReviewRemarkModel.inclined,
                'content': $scope.addFirstReviewRemarkModel.content
              }
            ].concat($scope.firstReviewProjectRemarksList);
          }
          else
            alert('评价失败: ' + data.message);
          console.log(data);
        },
        function error() {
          alert('添加失败, 检查一下网络配置看看?');
        }
      );

    };

    /* 添加终审评审信息 */
    $scope.addFinalReviewRemarkInfo = function() {
      reviewFactory.newFinalReviewRemark(sessionService.headers()).post(
        {
          "projectId": $stateParams.pid,
          "phase": "FINAL_REVIEW",
          "managerName": $scope.addFinalReviewRemarkModel.managerName,
          "content": $scope.addFinalReviewRemarkModel.content,
          "inclined": $scope.addFinalReviewRemarkModel.inclined,
          "assessedValue": $scope.addFinalReviewRemarkModel.highestEvaluation,
          "investAmount": $scope.addFinalReviewRemarkModel.maxInvestment,
          "invested": $scope.addFinalReviewRemarkModel.leadInvest
        },
        function success(data) {
          if(data.code === '200') {
            alert('添加评审内容成功.');
            $scope.finalReviewProjectRemarksList = [
              {
                'uid': null,
                'managerName': $scope.addFinalReviewRemarkModel.managerName,
                'like': $scope.addFinalReviewRemarkModel.inclined,
                'content': $scope.addFinalReviewRemarkModel.content,
                upInvestment: $scope.addFinalReviewRemarkModel.maxInvestment,
                highestValuation: $scope.addFinalReviewRemarkModel.highestEvaluation,
                investType: 'S',
                leadInvestor: $scope.addFinalReviewRemarkModel.leadInvest
              }
            ].concat($scope.finalReviewProjectRemarksList);
          }
          else
            alert('评价失败: ' + data.message);
          console.log(data);
        },
        function error(data) {

        }
      );
    };

    $scope.changeToObserve = function() {
      // $scope.changeProjectPhase('')
    };

    /* Initialize */
    $scope.initializeFetchData();

  });
