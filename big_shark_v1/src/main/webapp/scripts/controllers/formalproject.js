/**
 * Created by joshoy on 15/8/1.
 */

'use strict';

function datetime_to_unix(datetime){
  var tmp_datetime = datetime.replace(/:/g,'-');
  tmp_datetime = tmp_datetime.replace(/ /g,'-');
  var arr = tmp_datetime.split("-");
  var now = new Date(Date.UTC(arr[0],arr[1]-1,arr[2],arr[3]-8,arr[4],arr[5]));
  return parseInt(now.getTime()/1000);
}

angular.module(COFIG_APP_NAME)
  .controller('formalProjectController',
  function($scope, $stateParams, $upload, sessionService,
           projectFactory, equityFactory, progressFactory, founderFactory) {

    $scope.investType = [
      'ORIGINAL', 'ADDITIONAL', 'TRANSFER'
    ];
    $scope.investCurrencyList = [
      'USD', 'RMB'
    ];
    $scope.progressType = [
      'USER_PROMOTE',
      'VERSION_UPDATE',
      'MEDIA_COVERAGE',
      'FINANCE_NEGOTIATE',
      'BUSINESS_COOPERATE',
      'CONTEST_AWARD'
    ];
    $scope.investTypeMap = {
      'ORIGINAL': '原始投资',
      'ADDITIONAL': 'Additional',
      'TRANSFER': 'Transfer'
    };
    $scope.eventTypeMap = {
      'VERSION_UPDATE': {name: '版本更新', className: 'progress-type-update'},
      'USER_PROMOTE': {name: '用户推广', className: 'progress-type-spread'},
      'MEDIA_COVERAGE': {name: '媒体报道', className: 'progress-type-media'},
      'FINANCE_NEGOTIATE': {name: '融资洽谈', className: 'progress-type-financing'},
      'BUSINESS_COOPERATE': {name: '业务合作', className: 'progress-type-cooperation'},
      'CONTEST_AWARD': {name: '比赛获奖', className: 'progress-type-awarding'}
    };
    $scope.monthMap = {
      1: '1月',
      2: '2月',
      3: '3月',
      4: '4月',
      5: '5月',
      6: '6月',
      7: '7月',
      8: '8月',
      9: '9月',
      10: '10月',
      11: '11月',
      12: '12月'
    };
    $scope.yearList = [
      2010, 2011, 2012, 2013, 2014, 2015
    ];
    $scope.yearMap = {
      2010: '2010年',
      2011: '2011年',
      2012: '2012年',
      2013: '2013年',
      2014: '2014年',
      2015: '2015年'
    };
    $scope.progressInfoMap = {
      'USER_PROMOTE': '用户推广',
      'VERSION_UPDATE': '版本更新',
      'MEDIA_COVERAGE': '媒体报道',
      'FINANCE_NEGOTIATE': '融资洽谈',
      'BUSINESS_COOPERATE': '业务合作',
      'CONTEST_AWARD': '比赛获奖'
    };


    $scope.changeCurrentViewingEquityId = function(eid) {
      $scope.status.currentViewingEquityId = eid;
      // console.log('$scope.status.currentViewingEquityId changed to ' + eid + '.');
    };


    $scope.status = {
      projectName:'载入中...',
      projectIntroduction: '载入中...',
      projectLogoUrl: 'images/company-logo-demo.png',
      projectStartTime: 0,
      projectStartCity: '载入中...',
      projectTags: [],

      progressInfoLoaded: false,

      currentViewingEquityId: -1,

      equityInfoList: [],

      progressInfoList: [],

      founderList: [],

      evaluationList: [
        {
          'managerName': '项目经理A',
          'time': '2015-06-25 14:25',
          'content': '商业模式和切入点都很有趣! 看好优点这个app!'
        },
        {
          'managerName': '项目经理B',
          'time': '2015-06-26 18:25',
          'content': '商业模式和切入点都很有趣! 看好优点这个app!'
        }
      ],

      /* post form models */
      equityModel: {
        investor: '',
        year: 2015,
        month: 9,
        investType: 'ORIGINAL',
        investCurrency: 'RMB',
        investAmount: '',
        estimateAmount: '',
        sharePercentage: '%',
        board: ''
      },

      newProgressModel: {
        type: 'USER_PROMOTE',
        content: ''
      },

      newFounderModel: {
        name: '',
        mobile: '',
        wechat: '',
        email: '',
        idNumber: '',
        position: ''
      }
    };

    $scope.clearEquityModel = function() {
      $scope.status.equityModel = {
        investor: '',
        year: 2015,
        month: 9,
        investType: 'ORIGINAL',
        investCurrency: 'RMB',
        investAmount: '',
        estimateAmount: '',
        sharePercentage: '',
        board: ''
      };
    };

    /* 显示公司的Tags */
    $scope.showProjectTags =  function() {
      var ret = '';
      for (var i = 0; i < $scope.status.projectTags.length; i++) {
        var tag = $scope.status.projectTags[i];
        ret = ret + tag;
        if (i !== $scope.status.projectTags.length - 1) {
          ret = ret + ', ';
        }
      }
      return ret;
    };

    $scope.timeStampToDate = function(timestamp){
      var newdate = new Date(timestamp);
      return newdate.toLocaleDateString();
    };


    /* Initialize */

    /* 刷新项目信息 */
    $scope.refreshProjectInfo = function() {
      projectFactory.getFormalProjectInfo(sessionService.headers(), $stateParams.pid).get(
        {},
        function success(data) {
          if (data.code !== '200') {
            alert('获取项目信息失败...错误代码: ' + data.code + '\n错误信息: ' + data.message);
            return null;
          }
          /* else if success */
          var info = data.data;
          console.log(info);
          $scope.status.projectName = info.name;
          $scope.status.projectIntroduction = info.description;
          $scope.status.projectStartCity = info.city;
          $scope.status.projectTags = info.type.split(',');
          $scope.status.projectStartTime = info.startDate;
        },
        function error() {
          alert('获取项目信息失败, 检查一下网络设置看看? ');
        }
      );
    };

    /* 刷新股权信息 */
    $scope.refreshEquityInfo = function() {
      equityFactory.readEquityInfo(sessionService.headers(), $stateParams.pid).get(
        {},
        function success(data) {
          if (data.code !== '200') {
            alert('获取项目信息失败...错误代码: ' + data.code + '\n错误信息: ' + data.message);
            return null;
          }
          /* else if success */
          var info = data.data;
          $scope.status.equityInfoList = [];
          for (var i = 0; i < info.length; i++) {
            $scope.status.equityInfoList = $scope.status.equityInfoList.concat(info[i]);
          }
        },
        function error() {
          // TODO
        }
      );
    };

    /* 新增股权信息 */
    $scope.addEquityInfo = function() {
      console.log($scope.status.equityModel);

      equityFactory.newEquityInfo(sessionService.headers()).post({
        "projectId": $stateParams.pid,
        'investor': $scope.status.equityModel.investor,
        'investTime': $scope.status.equityModel.year + '-' + $scope.status.equityModel.month.toString() + '-1',
        'investType': $scope.status.equityModel.investType,
        'investCurrency': $scope.status.equityModel.investCurrency,
        'investAmount': $scope.status.equityModel.investAmount,
        'estimateAmount': $scope.status.equityModel.estimateAmount,
        'sharePercentage': $scope.status.equityModel.sharePercentage,
        'board': $scope.status.equityModel.board
      },
      function success(data) {
        if (data.code === '200') {
          console.log('股权信息上传成功~');
        }
        else {
          console.log('股权信息上传失败, 错误信息: ' + data.message);
        }
      },
      function error() {
        alert('创建失败, 请检查网络环境配置...');
      });
    };

    /* 上传股权信息附件
    /*
     * one of BIZ_LICENSE, ORG_CODE_CERT, TAX_REG_CERT,
     *        CORP_ARTICLE, SHAREHOLDER_RESOLUTE, INVEST_AGREE, AUDIT_REPORT
     *
     * */
    $scope.uploadEquityAttachment = function(files, type, eid) {
      for (var i = 0; i < files.length; i++) {
        var file = files[i];
        console.log('将要上传的文件: ' + file);
        $upload.upload({
          url: HOST_URL + '/api/equity/' + eid +'/file',
          headers: sessionService.headers(),
          file: file,
          fileFormDataName: 'file'
        }).progress(
          function(evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            console.log('上传进度: ' + progressPercentage.toString() + '%');
          }
        ).success(
          function(data) {
            if (data.code !== '200') {
              alert('上传失败: ' + data.message);
              return null;
            }
            console.log('上传成功!');
          }
        );
      }
    };

    /* 刷新项目进展 */
    $scope.refreshProgressInfo = function() {

      $scope.status.progressInfoLoaded = false;
      $scope.status.progressInfoList = [];

      progressFactory.readProjectProgress(sessionService.headers(), $stateParams.pid).get(
        {},
        function success(data) {
          if (data.code !== '200') {
            alert('获取项目进展信息失败: ' + data.message);
            return null;
          }
          console.log('已成功获取项目进展信息.');
          console.log(data);
          $scope.status.progressInfoLoaded = true;
          var infoList = data.data;
          for (var i = 0; i < infoList.length; ++i) {
            $scope.status.progressInfoList = $scope.status.progressInfoList.concat({
              id: infoList[i].id,
              projectId: $stateParams.pid,
              eventType: infoList[i].eventType,
              description: infoList[i].description
            });
          }
        },
        function error() {
          alert('获取项目进展信息失败……检查一下网络看看? ');
        }
      );
    };

    /* 新增项目进展 */
    $scope.addProgressInfo = function() {
      progressFactory.newProjectProgress(sessionService.headers()).post(
        {
          'projectId': $stateParams.pid,
          'eventType': $scope.status.newProgressModel.type,
          'description': $scope.status.newProgressModel.content
        },
        function success(data) {
          alert('添加成功~');
          $scope.refreshProgressInfo();
        },
        function error() {
          alert('添加项目进展失败……检查一下网络看看? ');
        }
      );
    };

    /* 刷新创始人列表信息 */
    $scope.refreshFounderInfo = function() {
      founderFactory.getProjectFounderInfo(sessionService.headers(), $stateParams.pid).get(
        {},
        function success(data) {
          var ls = data.data;
          for (var i = 0; i < ls.length; i++) {
            $scope.status.founderList = $scope.status.founderList.concat(ls[i]);
          }
        },
        function error() {
        }
      );
    };

    /* 添加创始人 */
    $scope.addFounder = function() {
      founderFactory.addProjectFounder(sessionService.headers()).post(
        {
          'projectId': $stateParams.pid,
          'name': $scope.status.newFounderModel.name,
          'mobile': $scope.status.newFounderModel.mobile,
          'wechat': $scope.status.newFounderModel.wechat,
          'email': $scope.status.newFounderModel.email,
          'idNumber': $scope.status.newFounderModel.idNumber,
          'position': $scope.status.newFounderModel.position
        },
        function success(data) {
          if (data.code === '200') {
            alert('添加创始人信息成功~');
            $scope.status.newFounderModel = {
                name: '',
                mobile: '',
                wechat: '',
                email: '',
                idNumber: '',
                position: ''
            };
          }
          else {
            alert('出错了: ' + code.message);
          }
        },
        function error() {
          alert('哎呀, 好像网络有些问题, 检查一下看看? ');
        }
      );
    };

    $scope.refreshProjectInfo();
    $scope.refreshEquityInfo();
    $scope.refreshProgressInfo();
    $scope.refreshFounderInfo();
  });
