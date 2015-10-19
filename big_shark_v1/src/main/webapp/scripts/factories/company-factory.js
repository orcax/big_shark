'use strict';


angular.module(COFIG_APP_NAME)
  .factory('founderFactory', function($resource, $rootScope) {

    this.readCompanyInfo = function(headers, projectId) {
      return $resource(HOST_URL + '/api/company' + '/?projectId=' + projectId.toString(),
        {},
        {
          'get': {
            method: 'GET',
            headers: headers
          }
        });
    };

    /*
    添加项目对应的公司信息。每个项目对应唯一的公司信息，所以同一个项目不能添加多个公司记录。当且仅当项目阶段是ACCEPTED时允许此操作。
    */
    this.newCompanyInfo = function(headers) {
      return $resource(HOST_URL + '/api/company/',
        {
          "projectId": "@projectId", // "项目ID | integer | required",
          "name": "@name", // "公司名称 | string | optional",
          "registerType": "@registerType", // "公司注册类型 | one of NATIONAL, SINO_FOREIGN, OTHER | optional",
          "registerAddress": "@registerAddress", // "注册地址 | string | optional",
          "registerTime": "@registerTime", // "注册时间 | date | optional",
          "registerCapital": "@registerCapital", // "注册资本 | string | optional",
          "legalPerson": "@legalPerson", // "法定代表人 | string | optional",
          "legalMobile": "@legalMobile", // "法定代表人手机号码 | string | optional",
          "legalEmail": "@legalEmail", // "法定代表人电子邮箱 | string | optional",
          "officeAddress": "@officeAddress" // "办公地址 | string | optional"
        },
        {
          'post': {
            method: 'POST',
            headers: headers
          }
        });
    };

    this.updateCompanyInfo = function(headers, companyId) {
      return $resource(HOST_URL + '/api/company/' + companyId,
        {
          "name": "@name", // "公司名称 | string | optional",
          "registerType": "@registerType", // "公司注册类型 | one of NATIONAL, SINO_FOREIGN, OTHER | optional",
          "registerAddress": "@registerAddress", // "注册地址 | string | optional",
          "registerTime": "@registerTime", // "注册时间 | date | optional",
          "registerCapital": "@registerCapital", // "注册资本 | string | optional",
          "legalPerson": "@legalPerson", // "法定代表人 | string | optional",
          "legalMobile": "@legalMobile", // "法定代表人手机号码 | string | optional",
          "legalEmail": "@legalEmail", // "法定代表人电子邮箱 | string | optional",
          "officeAddress": "@officeAddress" // "办公地址 | string | optional"
        },
        {
          'put': {
            method: 'PUT',
            headers: headers
          }
        });
    };

    return this;
});
