'use strict';

angular.module(COFIG_APP_NAME)
  .factory('founderFactory', function($resource, $rootScope) {

    // URL: /founder
    this.getProjectFounderInfo = function(headers, projectId) {
      return $resource(HOST_URL + '/api/founder?projectId=' + projectId.toString(),
        {},
        {
          'get': {
            'method': 'GET',
            'headers': headers
          }
        });
    };

    this.addProjectFounder = function(headers) {
      return $resource(HOST_URL + '/api/founder',
        {
          "projectId": '@projectId',
          "name": "@name", //"创始人姓名 | string | optional",
          "mobile": "@mobile", // "手机号 | string | optional",
          "wechat": "@wechat", //"微信号 | string | optional",
          "email": "@email", // "电子邮箱 | string | optional",
          "idNumber": "@idNumber", // "身份证/护照号码 | string | optional",
          "position": "@position" // "目前职位 | string | optional"
        },
        {
          'post': {
            'method': 'POST',
            'headers': headers
          }
        });
    };

    // URL: /founder/{fid}

    this.readFounderInfo = function(headers, founderId) {
      return $resource(HOST_URL + '/api/founder/' + founderId,
        {},
        {
          'get': {
            'method': 'GET',
            'headers': headers
          }
        });
    };

    this.updateFounderInfo = function(headers, founderId) {
      return $resource(HOST_URL + '/api/founder/' + founderId,
        {
          "name": "@name", //"创始人姓名 | string | optional",
          "mobile": "@mobile", // "手机号 | string | optional",
          "wechat": "@wechat", //"微信号 | string | optional",
          "email": "@email", // "电子邮箱 | string | optional",
          "idNumber": "@idNumber", // "身份证/护照号码 | string | optional",
          "position": "@position" // "目前职位 | string | optional"
        },
        {
          'put': {
            'method': 'PUT',
            'headers': headers
          }
        });
    };

    this.deleteFounderInfo = function(headers, founderId) {
      return $resource(HOST_URL + '/api/founder/' + founderId,
        {},
        {
          'delete': {
            'method': 'DELETE',
            'headers': headers
          }
        });
    };

    // URL: /founder/{fid}/work

    this.readFounderWorkExp = function(headers, founderId) {
      return $resource(HOST_URL + '/api/founder/' + founderId + '/work',
        {},
        {
          'get': {
            'method': 'GET',
            'headers': headers
          }
        });
    };

    this.newFounderWorkExp = function(headers, founderId) {
      return $resource(HOST_URL + '/api/founder/' + founderId + '/work',
        {
          "company": "@company", // "工作单位 | string | optional",
          "position": "@position", // "职位 | string | optional",
          "startDate": "@startDate", // "开始时间 | string | optional",
          "endDate": "@endDate" // "结束时间 | string | optional"
        },
        {
          'post': {
            'method': 'POST',
            'headers': headers
          }
        });
    };

    this.updateFounderWorkExp = function(headers, founderId, workExpId) {
      return $resource(HOST_URL + '/api/founder/' + founderId + '/work/' + workExpId,
        {
          "company": "@company", // "工作单位 | string | optional",
          "position": "@position", // "职位 | string | optional",
          "startDate": "@startDate", // "开始时间 | string | optional",
          "endDate": "@endDate" // "结束时间 | string | optional"
        },
        {
          'put': {
            'method': 'POST',
            'headers': headers
          }
        });
    };

    this.deleteFounderWorkExp = function(headers, founderId, workExpId) {
      return $resource(HOST_URL + '/api/founder/' + founderId + '/work/' + workExpId,
        {},
        {
          'delete': {
            'method': 'DELETE',
            'headers': headers
          }
        });
    };

    // URL: /founder/{fid}/edu

    this.readFounderEducationInfo = function(headers, founderId) {
      return $resource(HOST_URL + '/api/founder/' + founderId + '/edu',
        {},
        {
          'get': {
            'method': 'GET',
            'headers': headers
          }
        });
    };

    this.newFounderEducationInfo = function(headers, founderId) {
      return $resource(HOST_URL + '/api/founder/' + founderId + '/edu',
        {
          "college": "@college", // "毕业院系 | string | optional",
          "major": "@major", // "专业 | string | optional",
          "startDate": "@startDate", // "开始时间 | string | optional",
          "endDate": "@endDate" // "结束时间 | string | optional"
        },
        {
          'post': {
            'method': 'POST',
            'headers': headers
          }
        });
    };

    this.updateFounderEducationInfo = function(headers, founderId, eduInfoId) {
      return $resource(HOST_URL + '/api/founder/' + founderId + '/edu/' + eduInfoId,
        {
          "college": "@college", // "毕业院系 | string | optional",
          "major": "@major", // "专业 | string | optional",
          "startDate": "@startDate", // "开始时间 | string | optional",
          "endDate": "@endDate" // "结束时间 | string | optional"
        },
        {
          'put': {
            'method': 'PUT',
            'headers': headers
          }
        });
    };

    this.deleteFounderEducationInfo = function(headers, founderId, eduInfoId) {
      return $resource(HOST_URL + '/api/founder/' + founderId + '/edu/' + eduInfoId,
        {},
        {
          'delete': {
            'method': 'DELETE',
            'headers': headers
          }
        });
    };

    return this;

  });
