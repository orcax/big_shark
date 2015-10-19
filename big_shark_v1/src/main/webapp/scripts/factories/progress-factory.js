'use strict';

angular.module(COFIG_APP_NAME)
  .factory('progressFactory', function($resource, $rootScope) {

    // URL: /progress

    this.readProjectProgress = function(headers, projectId) {
      return $resource(HOST_URL + '/api/progress?projectId=' + projectId.toString(),
        {},
        {
          get: {
            method: 'GET',
            headers: headers
          }
        });
    };

    this.newProjectProgress = function(headers) {
      return $resource(HOST_URL + '/api/progress',
        {
          'projectId': '@projectId',
          'eventType': '@eventType',
          'description': '@description'
        },
        {
          'post': {
            method: 'POST',
            headers: headers
          }
        });
    };

    // this.newProjectProgress = function(headers) {

    // };

    return this;
});
