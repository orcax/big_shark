'use strict';

angular.module(COFIG_APP_NAME)
  .factory('equityFactory', function($resource, $rootScope) {

    this.readEquityInfo = function(headers, projectId) {
      return $resource(HOST_URL + '/api/equity' + '?projectId=' + projectId.toString(),
        {},
        {
          'get': {
            method: 'GET',
            headers: headers
          }
        });
    };

    this.newEquityInfo = function(headers) {
      return $resource(HOST_URL + '/api/equity',
        {
          "projectId": "@projectId", // "项目ID | integer | required",
          "investor": "@investor", // "投资人/投资机构 | integer | optional",
          "investTime": "@investTime", // "注册时间 | date | optional",
          "investType": "@investType", // "投资类型 | one of ORIGINAL, ADDITIONAL, TRANSFER | optional",
          "investCurrency": "@investCurrency", // "投资币种 | one of RMB, USD | optional",
          "investAmount": "@investAmount", // "投资金额 | string | optional",
          "estimateAmount": "@estimateAmount", // "投后估值 | string | optional",
          "sharePercentage": "@sharePercentage", // "占股比例 | string | optional",
          "board": "@board", // "董事会相关 | string | optional"
        },
        {
          'post': {
            method: 'POST',
            headers: headers
          }
        });
    };

    this.editEquityInfo = function(headers, equityId) {
      return $resource(HOST_URL + '/api/equity/' + equity.toString(),
        {
          "projectId": '@projectId',
          "investor": "@investor", // "投资人/投资机构 | integer | optional",
          "investTime": "@investTime", // "注册时间 | date | optional",
          "investType": "@investType", // "投资类型 | one of ORIGINAL, ADDITIONAL, TRANSFER | optional",
          "investCurrency": "@investCurrency", // "投资币种 | one of RMB, USD | optional",
          "investAmount": "@investAmount", // "投资金额 | string | optional",
          "estimateAmount": "@estimateAmount", // "投后估值 | string | optional",
          "sharePercentage": "@sharePercentage", // "占股比例 | string | optional",
          "board": "@board" // "董事会相关 | string | optional"
        },
        {
          'put': {
            method: 'PUT',
            headers: headers
          }
        });
    };

    this.deleteEquityInfo = function(headers, equityId) {
      return $resource(HOST_URL + '/api/equity/' + equityId.toString(),
        {},
        {
          'delete': {
            method: 'DELETE',
            headers: headers
          }
        });
    };

    this.deleteEquityAttachment = function(headers, equityId, fileId) {
      return $resource(HOST_URL + '/api/equity/' + equityId + '/file/' + fileId,
        {},
        {
          'delete': {
            method: 'DELETE',
            headers: headers
          }
        });
    };

    return this;

});
