'use strict';

/* 用户所有projects列表展示时的controller */
angular.module(COFIG_APP_NAME)
  .controller('ProjectsController',
  function ($scope, $localStorage, qService, $route,
            sessionService, projectFactory) {

    $scope.userProjectsList = [];
    $scope.userRealName = $localStorage.currentUser.name;

    $scope.listLoaded = false;

    $scope.projectStatusMap = {
      'DRAFT': '草稿',
      'AUDITION': '海选中',
      'FIRST_REVIEW': '初审中',
      'FINAL_REVIEW': '终审中',
      'ACCEPTED': '正式项目'
    };

    console.log('开始获取用户项目列表...');

    $scope.changeEditingProject = function(project) {
      $localStorage.currentEditingProject = project;
      console.log('当前编辑项目已更新: ');
      console.log($localStorage.currentEditingProject);
    };

    $scope.getList = function() {
      projectFactory.listProject(sessionService.headers()).get(
       {},
       function success(data, headers) {
         console.log('获取成功!');
         console.log(data);
         $scope.listLoaded = true;
         $scope.userProjectsList = data.data;
       },
       function error(data) {
         console.log('获取失败...');
       });
    };


    $scope.getList();

  });


/* 创建项目用controller */
angular.module(COFIG_APP_NAME)
  .controller('CreateProjectController',
  function ($scope,  $localStorage, qService, $location,
            sessionService, projectFactory, accountFactory) {
    //var myAppModule = angular.module('MyApp', ['selectize']);
    /* 用户是否发送过验证码 */
    $scope.sentCaptcha = false;

    /* 保护Submit Button不被多次点击 */
    $scope.submitBtnClicked = false;
    $scope.recoverSubmitButton = function() {
      $scope.submitBtnClicked = false;
    };

  	$scope.years = ["2015","2014","2013","2012","2011","2010"];
  	$scope.months = ["1","2","3","4","5","6","7","8","9","10","11","12"];
  	$scope.cities = ["上海","杭州","深圳"];
  	$scope.types = ["电子商务","O2O","社交","云计算"];

  	$scope.form = {
  		name: "",
  		contact_name: "",
  		contact_email: "",
  		contact_phone: "",
  		verification_code: "",
  		year: "",
  		month: "",
  		city: "",
  		type: [],
  		description: ""
  	};

    /* selectize设置 */
    $scope.selectizeConfig = {
      create: true,
      maxItems: 3,
      placeholder: '请输入, 最多三项',
      plugins: ['remove_button'],
      allowEmptyOption: true
    };

  	var form = $scope.form = {};

    /* 提交创建的项目 */
  	$scope.register = function () {
      if ($scope.submitBtnClicked) {
        return null;
      }

      if (!($scope.form.name
        && $scope.form.contact_email
        && $scope.form.contact_phone
        && $scope.form.verification_code
        && $scope.form.year
        && $scope.form.month
        && $scope.form.city
        && $scope.form.description)) {
        alert('表单没有填完!');
        // TODO: 在页面上显示提示表单未填完
        return null;
      }

      var submitForm = {
        name: $scope.form.name,
        contactName: $scope.form.contact_name,
        contactEmail: $scope.form.contact_email,
        contactMobile: $scope.form.contact_phone,
        startDate: $scope.form.year + '-' + $scope.form.month + '-1',
        city: $scope.form.city,
        type: '',
        description: $scope.form.description || ''
      };

      console.log($scope.form.type);
      /* 将$scope.form.type从array转换为string */
      if (!$scope.form.type) {
        alert('请给您的项目加个标签哦~');
        return null;
      }
      if ($scope.form.type.length !== 0) {
        for (var i = 0; i < $scope.form.type.length; i++) {
          submitForm.type = submitForm.type + $scope.form.type[i];
          if (i !== $scope.form.type.length - 1) {
            submitForm.type = submitForm.type + ',';
          }
        }
      }
      /* 锁住提交项目按钮以防用户按多次 */
      $scope.submitBtnClicked = true;
      setTimeout(function(){ $scope.submitBtnClicked = false; console.log("Recovered!"); }, 2000);

      var f = function () {
        projectFactory.createProject(sessionService.headers()).post(
          submitForm,
          function success(data, headers) {
            alert('创建成功!');
            $location.path('/projects');
            $route.reload();
          },
          function error(data) {
            alert('创建失败, 请检查网络连接是否正常?');
            console.log(data);
          }
        );
      };

      $scope.verifyCaptcha($scope.form.verification_code, f);
      // console.log('Done!');
  	};


    /* 发送验证码 */
    $scope.sendCaptcha = function() {
      if (!$scope.form.contact_phone) {
        alert('请输入手机号!');
      }

      var header = sessionService.headers();
      header['Content-type'] = 'application/x-www-form-urlencoded';

      accountFactory.sendCaptcha(header).post(
        {
          'leaderMobile': $scope.form.contact_phone
        },
        function success(data, headers) {
          $scope.sentCaptcha = true;
          console.log('成功发送验证码!');
          console.log(data);
        },
        function error(data) {
          console.log('发送验证码失败!');
          console.log(data);
        }
      );
    };

    $scope.verifyCaptcha = function(code, functionAfterSuccess) {
      if(!$scope.sentCaptcha) {
        alert('创建项目前, 请先将验证码发送到您的手机上并输入哦~');
        return null;
      }

      var header = sessionService.headers();
      header['Content-type'] = 'application/x-www-form-urlencoded';

      accountFactory.verifyCaptcha(header).put(
        {
          'leaderMobile': $scope.form.contact_phone,
          'code': code
        },
        function success(data, headers) {
          if (data === '200') {
            console.log('验证码正确!');
          } else {
            alert('验证码错误!');
            return null;
          }

          functionAfterSuccess();
        },
        function error(data) {
          alert('验证码验证失败, 请检查网络配置!');
          console.log(data);
        }
      );
    };

    /* debug only */
    $scope.checkObject = function() {
      console.log('将要提交的表单如下:');
      console.log($scope.form);
    };

  });


/* 编辑项目用controller */
angular.module(COFIG_APP_NAME)
  .controller('EditProjectController',
  function ($scope,  $localStorage, qService, $location, $window,
            sessionService, projectFactory, accountFactory,
            $upload) {
  	$scope.years = ["2015","2014","2013","2012","2011","2010"];
  	$scope.months = ["1","2","3","4","5","6","7","8","9","10","11","12"];
  	$scope.cities = ["上海","杭州","深圳"];
  	$scope.types = ["电子商务","O2O","社交","云计算"];
    $scope.attachmentCurrent = "";
    $scope.uploadInfo = '';



    $scope.getHostUrl = function() {
      return HOST_URL;
    };
    $scope.getProjectId = function() {
      return $localStorage.currentEditingProject.id;
    };

    /* 下载附件 */
    $scope.spanDownloadAttachmentUrl = function(fid, filename) {
      return projectFactory.downloadAttachment(sessionService.headers(),
            $localStorage.currentEditingProject.id, fid, filename).get(
        {},
        function success(data) {
          // TODO
          console.log(data);
        },
        function error() {
          // TODO
        }
      ).$promise.then(
        function download(data) {
          var blob = data.response.blob;
          $window.saveAs(blob, 'unnamed');
          $window.openSaveAsDialog
        }
      );
    };

    $scope.project = {
  		status: "草稿",
  		name: $localStorage.currentEditingProject.name,
  		contact_name: $localStorage.currentEditingProject.contactName,
  		contact_email: $localStorage.currentEditingProject.contactEmail,
  		contact_phone: $localStorage.currentEditingProject.contactMobile,
  		year: "2015",
  		month: "4",
  		city: $localStorage.currentEditingProject.city,
  		type: $localStorage.currentEditingProject.type.split(','),
  		description: $localStorage.currentEditingProject.description
  	};

    $scope.selectizeConfig = {
      create: true,
      items: $localStorage.currentEditingProject.type.split(','),
      maxItems: 3,
      placeholder: '请输入, 最多三项',
      plugins: ['remove_button'],
      allowEmptyOption: true
    };

    $scope.fileList = [];

    console.log('当前Object: ');
    console.log($localStorage.currentEditingProject);
    console.log('当前项目的pid: %d', $localStorage.currentEditingProject.id);
    console.log($scope.project.type);
    $scope.projectStatus = $localStorage.currentEditingProject.phase;
    console.log('PHASE = ' + $scope.projectStatus);

    /* 获取当前项目的附件清单并更新model */
    $scope.getProjectFileList = function() {
      projectFactory.getProjectFileList( sessionService.headers(),
                                         $localStorage.currentEditingProject.id)
      .get(
        {},
        function success(data, headers) {
          console.log('成功获取附件列表!');
          console.log(data);

          $scope.fileList = data.data;
        },
        function error(data) {
          console.log('获取附件列表失败...');
        }
      );
    };


    /* 上传附件 */
    $scope.uploadAttachment = function(files) {
      if (files && files.length) {
        for (var i = 0; i < files.length; i++) {
          var file = files[i];
          console.log('将要上传的文件: ');
          console.log(file);
          /* 上传当前文件 */
          $upload.upload({
            url: HOST_URL + '/api/project/' + $localStorage.currentEditingProject.id + '/file',
            headers: sessionService.headers(),
            file: file,
            fileFormDataName: 'file'
          }).progress(
            /* 上传过程函数 */
            function(evt) {
              var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
              console.log('上传进度: ' + progressPercentage.toString() + '%');
              $scope.uploadInfo = '上传进度: ' + progressPercentage.toString() + '%';
            }
          ).success(
            /* 上传成功 */
            function(data, status, headers, config) {
              //console.log('文件上传成功!');
              //console.log('文件上传成功返回数据: ');
              //console.log(data);
              if (data.code !== '200') {
                alert(data.message);
              }
              $scope.uploadInfo = '';
              $scope.getProjectFileList();
          });
        }
      }

    };

    /* 删除附件, fid: integer */
    $scope.deleteAttachment = function(fid) {
      var confirmDelete = confirm("是否确认删除该附件?");
      if (!confirmDelete) {
        return null;
      }
      projectFactory.deleteAttachment(sessionService.headers(),
                                      $localStorage.currentEditingProject.id,
                                      fid)
        .delete(
          {},
          function success(data) {
            alert('删除附件成功!');
            $scope.getProjectFileList();
            console.log(data);
          },
          function error(data) {
            alert('删除失败, 请检查网络是否正常.');
            console.log(data);
            $scope.getProjectFileList();
          }
        );
    };

    /* 保存修改 */
    $scope.updateProject = function() {
      var updateType = '';
      for (var i = 0; i < $scope.project.type.length; i++) {
        updateType += $scope.project.type[i];
        if (i !== $scope.project.type.length - 1) {
          updateType += ',';
        }
      }
      projectFactory.updateProject(sessionService.headers(),
                                   $localStorage.currentEditingProject.id)
      .put(
        {
          "contactName": $scope.project.contact_name,
          "contactEmail": $scope.project.contact_email,
          "contactMobile": $scope.project.contact_phone,
          "name": $scope.project.name,
          "startDate": $scope.project.year + '-' + $scope.project.month + '-1',
          "city": $scope.project.city,
          "type": updateType,
          "description": $scope.project.description
        },
        function success(data) {
          alert('项目草稿保存成功!');
        },
        function error() {
          alert('网络似乎有些问题, 请重试...');
        });
    };


    /* 确认提交 */
    $scope.submitProject = function() {
      projectFactory.submitProject(sessionService.headers(),
                                   $localStorage.currentEditingProject.id)
        .post(
        {
          'pid': $localStorage.currentEditingProject.id
        },
        function success(data) {
          alert('草稿项目提交成功.');
          $location.path('/projects');
          $route.reload();
        },
        function error() {
          alert('提交失败...请检查字段是否完整或者网络是否连接.');
        }

        );
    };

    /* 删除项目 */
    $scope.deleteProject = function() {
      var confirmDelete = confirm("是否确定要删除这个项目草稿? 一旦删除将无法复原!");
      if (!confirmDelete) { console.log('已取消.'); return null; }
      projectFactory.deleteProject(sessionService.headers(),
                                   $localStorage.currentEditingProject.id)
        .delete(
        {},
        function success(data) {
          alert('项目已删除.');
          $location.path('/projects');
          $route.reload();
        },
        function error() {
          alert('网络错误, 请检查当前网络是否正常...');
        }
      );
    };

    $scope.getProjectFileList();

  });
