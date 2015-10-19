/**
 * Created by joshoy on 15/8/15.
 */

angular.module(COFIG_APP_NAME).factory('reviewFactory', function($resource) {

  this.getReviewLists = function(headers) {
    return $resource(HOST_URL + '/api/review',
    {
      projectId: '@projectId',
      phases: '@phases'
    },
    {
      'get': {
        method: 'GET',
        headers: headers
      }
    }
    );
  };

  this.searchReviewLists = function(headers) {
    return $resource(HOST_URL + '/api/project/search',
      {
        phase: '@phase',
        status: '@status',
        city: '@city',
        name: '@name',
        notified: '@notified',
        investType: '@investType',
        pageSize: 100000,
        pageNumber: 1
      },
      {
        'get': {
          method: 'GET',
          headers: headers
        }
      }
    );
  };

  this.getReviewDetailById = function(pid, headers) {
    return $resource(HOST_URL + '/api/project/' + pid.toString(),
    {},
    {
      'get': {
        method: 'GET',
        headers: headers
      }
    });
  };

  this.addReviewInfo = function(rid, postContentMap, headers) {
    return $resource(HOST_URL + '/api/project/' + rid.toString(),
      postContentMap,
      {
        'put': {
          method: 'PUT',
          headers: headers
        }
      }
    );
  };

  this.getProjectReviewRemarkInfo = function(headers, projectId, phases) {
    return $resource(HOST_URL + '/api/review' + '?projectId=' + projectId + '&phases=' + phases,
      {},
      {
        'get': {
          method: 'GET',
          headers: headers
        }
      });
  };

  this.changeProjectPhase = function(headers, projectId, phase) {
    return $resource(HOST_URL + '/api/project/' + projectId + '/phase/' + phase,
      {
        'investType': '@investType'
      },
      {
        'post': {
          method: 'POST',
          headers: headers
        }
      });
  };

  this.setProjectNotified = function(headers, projectIds /* An array of integers */) {
    return $resource(HOST_URL + '/api/project/notify',
      {
        'projectIds': projectIds.toString()
      },
      {
        'post': {
          method: 'POST',
          headers: headers
        }
      });
  };

  this.newFirstReviewRemark = function(headers) {
    return $resource(HOST_URL + '/api/review',
      {
        'projectId': '@projectId',
        'phase': '@phase',
        'managerName': '@managerName',
        'content': '@content',
        'inclined': '@inclined'
      },
      {
        'post': {
          method: 'POST',
          headers: headers
        }
      });
  };

  this.newFinalReviewRemark = function(headers) {
    return $resource(HOST_URL + '/api/review',
      {
        "projectId": '@projectId',
        "phase": "@phase",
        "managerName": "@managerName",
        "content": "@content",
        "inclined": '@inclined',
        "assessedValue": '@assessedValue',
        "investAmount": "@investAmount",
        "invested": '@invested'
      },
      {
        'post': {
          method: 'POST',
          headers: headers
        }
      });
  };

  return this;
});
