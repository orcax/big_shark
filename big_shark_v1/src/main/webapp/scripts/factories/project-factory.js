/**
 * Created by joshoy on 15/8/10.
 */

'use strict';

angular.module(COFIG_APP_NAME).factory('projectFactory', function($resource) {

  /* 获取当前用户所有项目信息的工厂方法 */
  this.listProject = function(headers) {
    return $resource(HOST_URL + '/api/project', null, {
      'get': {
        method: 'GET',
        headers: headers
      }
    });
  };

  this.getProjectInfo = function(headers, projectId) {
    return $resource(HOST_URL + '/api/project/' + projectId.toString(), {}, {
      'get': {
        method: 'GET',
        headers: headers
      }
    });
  };

  this.createProject = function(headers) {
    return $resource(HOST_URL + '/api/project', null,
      {
        'post': {
          method: 'POST',
          headers: headers
        }
      }
    );
  };

  this.getProjectFileList = function(headers, pid) {
    return $resource(HOST_URL + '/api/project/' + pid.toString() + '/file',
    {
      pid: '@id'
    },
    {
      'get': {
        method: 'GET',
        headers: headers
      }
    }
    );
  };

  this.deleteAttachment = function(headers, pid, fid) {
    return $resource(HOST_URL + '/api/project/' + pid.toString() + '/file/' + fid.toString(),
      {},
      {
        'delete': {
          method: 'DELETE',
          headers: headers
        }
      }
    );
  };

  this.updateProject = function(headers, pid) {
    return $resource(HOST_URL + '/api/project/' + pid.toString(),
      {
        "contactName": '@contactName', // "项目负责人",
        "contactEmail": '@contactEmail', // "联系邮箱",
        "contactMobile": '@contactMobile', // "手机号码",
        "name": '@name', // "项目名称",
        "startDate": '@startDate', // "项目开始时间",
        "city": '@city', // "所在城市",
        "type": '@type', // "项目类型，多个字段请自行拼接成字符串",
        "description": '@description' // "项目简介"
      },
      {
        'put': {
          method: 'PUT',
          headers: headers
        }
      });
  };

  this.submitProject = function(headers, pid) {
    return $resource(HOST_URL + '/api/project/' + pid.toString() + '/submit',
      {
        pid: '@pid'
      },
      {
        'post': {
          method: 'POST',
          headers: headers
        }
      }
    );
  };

  this.deleteProject = function(headers, pid) {
    return $resource(HOST_URL + '/api/project/' + pid.toString(),
      {},
      {
        'delete': {
          method: 'DELETE',
          headers: headers
        }
      });
  };

  this.downloadAttachment = function(headers, pid, fid, saveFileName) {
    headers.accept = 'application/zip';

    return $resource(HOST_URL + '/api/project/' + pid + '/file/' + fid,
      {},
      {
        'get': {
          method: 'GET',
          headers: headers,
          responseType: 'arraybuffer',
          cache: false,
          transformResponse: function(data, headers) {
            var zip = null;
            if (data) {
              zip = new Blob([data], {
                type: 'application/zip' //or whatever you need, should match the 'accept headers' above
              });
            }

            //server should sent content-disposition header
            var fileName = saveFileName;
            var result = {
              blob: zip,
              fileName: fileName
            };

            return { response: result };
          }
        }
      });
  };

  /* Formal project part */

  this.getFormalProjectInfo = function(headers, projectId) {
    return $resource(HOST_URL + '/api/project/' + projectId.toString(),
      {},
      {
        'get': {
          method: 'GET',
          headers: headers
        }
      });
  };

  return this;
});
