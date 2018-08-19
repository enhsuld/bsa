altairApp
    .config([
        '$stateProvider',
        '$urlRouterProvider',
        '$httpProvider',
        function ($stateProvider, $urlRouterProvider, $httpProvider) {

            $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

            $stateProvider
                .state("restricted.pages.plan", {
                    url: "/validation",
                    templateUrl: 'app/custom/pages/staus/planView.html',
                    controller: 'planCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/custom/pages/staus/planController.js'
                            ]);
                        }],
                    },
                    data: {
                        pageTitle: 'Тулгалт'
                    }
                })
                .state("restricted.pages.userval", {
                    url: "/val",
                    templateUrl: 'app/custom/pages/staus/validationView.html',
                    controller: 'validationCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/custom/pages/staus/validationController.js'
                            ]);
                        }],
                        user_data: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/api/user'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        }

                    },
                    data: {
                        pageTitle: 'Хэрэглэгч'
                    }
                })
                .state("restricted.pages.nyabo", {
                    url: "/accountant",
                    templateUrl: 'app/custom/pages/nyabo/nyaboView.html',
                    controller: 'nyaboCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/custom/pages/nyabo/nyaboController.js'
                            ]);
                        }],
                        user_data: function ($http, $state, __env) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/api/user'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        }
                    },
                    data: {
                        pageTitle: 'Тайлан'
                    }
                })
                .state("restricted.pages.comuser", {
                    url: "/accounts",
                    templateUrl: 'app/custom/pages/schedule/pPuserView.html',
                    controller: 'userComCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/custom/pages/schedule/pPuserController.js'
                            ]);
                        }],
                        p_org: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutDepartment'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        p_role: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutRole'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        p_pos: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutPosition'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },

                    },
                    data: {
                        pageTitle: 'Хэрэглэгч'
                    }
                })

                .state("restricted.work", {
                    url: "/acc",
                    template: '<div ui-view autoscroll="false" ng-class="{ \'uk-height-1-1\': page_full_height }"/>',
                    abstract: true,
                    ncyBreadcrumb: {
                        label: 'Аудит'
                    }
                })

                .state("restricted.pages.integration", {
                    url: "/integration",
                    templateUrl: 'app/custom/pages/integration/integrationView.html',
                    controller: 'integrationCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_KendoUI',
                                'app/custom/pages/integration/integrationController.js'
                            ]);
                        }],
                        p_cat: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/fin/resource/LutCategory'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        decision: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/fin/resource/LutQuataDecision'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        reason: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/fin/resource/LutReason'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                    },
                    data: {
                        pageTitle: 'Нэгтгэл'
                    },
                    ncyBreadcrumb: {
                        label: 'Нэгтгэл'
                    }
                })

                .state("restricted.work.waudit", {
                    url: "/audit",
                    templateUrl: 'app/custom/pages/schedule/pwauditView.html',
                    controller: 'wauditCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_KendoUI',
                                'app/custom/pages/schedule/pwauditController.js'
                            ]);
                        }],
                        p_cat: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/fin/resource/LutCategory'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        decision: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/fin/resource/LutQuataDecision'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        reason: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/fin/resource/LutReason'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        }
                    },
                    data: {
                        pageTitle: 'Аудит'
                    },
                    ncyBreadcrumb: {
                        label: 'Хуваарьт ажил'
                    }
                })

                .state("restricted.work.mainwork", {
                    url: "/details/:issueId",
                    templateUrl: 'app/custom/pages/major/majorView.html',
                    controller: 'majorCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_dropify',
                                'lazy_KendoUI',
                                'app/custom/pages/major/majorController.js'
                            ]);
                        }],
                        mainobj: function ($http, $stateParams, $state) {
                            return $http({
                                method: 'GET',
                                url: __env.apiUrl() + '/fin/resource/MainAuditRegistration/' + $stateParams.issueId
                            })
                                .then(function (data) {
                                    if (data.data) {
                                        return data.data;
                                    } else {
                                        $state.go("error.404");
                                    }
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        worklist: function ($http, $stateParams, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/fin/resource/work'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                    },
                    data: {
                        pageTitle: 'Үндсэн самбар'
                    },
                    ncyBreadcrumb: {
                        label: 'Үндсэн самбар',
                        parent: 'restricted.work.waudit'
                    }
                })
                .state("restricted.work.accSurvey", {
                    url: "/survey/:planid/:formid/:levelid",
                    templateUrl: 'app/custom/journal/surveyView.html',
                    controller: 'surveyAccCtrl',

                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_KendoUI',
                                'lazy_ionRangeSlider',
                                'app/custom/journal/surveyAccController.js'
                            ], {serie: true});
                        }],
                        survey_dir: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/fin/resource/FinSurveyDirection'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        acc_data: function ($http, $stateParams) {
                            return $http({
                                method: 'GET',
                                url: __env.apiUrl() + '/fin/resource/LutForm/' + $stateParams.formid
                            })
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        app_data: function ($http, $stateParams) {
                            return $http({
                                method: 'GET',
                                url: __env.apiUrl() + '/fin/resource/MainAuditRegistration/' + $stateParams.planid
                            })
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },

                    },
                    data: {
                        pageTitle: 'Түүврийн жагсаалт'
                    },
                    ncyBreadcrumb: {
                        label: 'Түүврийн жагсаалт',
                        parent: function ($scope) {
                            return 'restricted.work.mainwork({issueId:' + $scope.from + '})';
                        }
                    }
                })


                .state("restricted.pages.quataperson", {
                    url: "/app/list",
                    templateUrl: 'app/custom/pages/schedule/pQuataPersonView.html',
                    controller: 'quatapersonCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_KendoUI',
                                'lazy_character_counter',
                                'app/custom/pages/schedule/pQuataPersonController.js'
                            ]);
                        }],
                        p_cat: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutCategory'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        decision: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutQuataDecision'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        reason: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutReason'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        users: function ($http, $state, user_data) {
                            return $http({
                                method: 'GET',
                                url: __env.apiUrl() + '/fin/resource/LutUser/' + user_data.depid
                            })
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                    },
                    data: {
                        pageTitle: 'Annual plan'
                    }
                })

                .state("restricted.pages.survey", {
                    url: "/survey",
                    templateUrl: 'app/custom/journal/surveyView.html',
                    controller: 'surveyCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_KendoUI',
                                'lazy_ionRangeSlider',
                                'app/custom/journal/surveyController.js'
                            ], {serie: true});
                        }],
                        survey_dir: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/fin/resource/FinSurveyDirection'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        totalAmount: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/fin/survey/amount/0/0'})
                                .then(function (data) {
                                    return data.data;
                                });
                        },
                        totalError: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/fin/survey/totalAccError/0/0'})
                                .then(function (data) {
                                    return data.data;
                                });
                        },

                    },
                    data: {
                        pageTitle: 'Түүврийн жагсаалт'
                    }
                })

                .state("restricted.pages.tryoutlist", {
                    url: "/tryOutlist",
                    templateUrl: 'app/custom/admin/information/pTryOutListView.html',
                    controller: 'tryOutlistCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_KendoUI',
                                'app/custom/admin/information/pTryOutListController.js'
                            ], {serie: true});
                        }],
                        work_type: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutCategory'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        audit_dir: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutAuditDir'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                    },
                    data: {
                        pageTitle: 'Горим сорил жагсаалт'
                    }
                })
                .state("restricted.pages.worklist", {
                    url: "/worklist",
                    templateUrl: 'app/custom/admin/system/pWorkListView.html',
                    controller: 'worklistCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_KendoUI',
                                'app/custom/admin/system/pWorkListController.js'
                            ]);
                        }],
                        au_work: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutAuditWork'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        audit_dir: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutAuditDir'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        au_levels: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutAuditLevel'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                    },
                    data: {
                        pageTitle: 'Ажлын жагсаалт'
                    }
                })
                .state("restricted.pages.workform", {
                    url: "/workaddform/:param",
                    templateUrl: 'app/custom/admin/system/WorkAddFormView.html',
                    controller: 'workAddformCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_dropify',
                                'lazy_ionRangeSlider',
                                'lazy_masked_inputs',
                                'lazy_character_counter',
                                'lazy_wizard',
                                'lazy_parsleyjs',
                                'app/custom/admin/system/WorkAddFormController.js'
                            ], {serie: true});
                        }],
                        audit_dir: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutAuditDir'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        au_work: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutAuditWork'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        work_type: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutCategory'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        au_level: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutAuditLevel'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        au_type: function ($http) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutReason'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        }
                    },
                    data: {
                        pageTitle: 'Ажил нэмэх'
                    }
                })
                .state("restricted.pages.puser", {
                    url: "/user",
                    templateUrl: 'app/custom/admin/pPuserView.html',
                    controller: 'userCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/custom/admin/pPuserController.js'
                            ]);
                        }],
                        p_role: function ($http, __env) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutRole'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },
                        p_plan: function ($http, __env) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutPlan'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                    $state.reload();
                                });
                        },

                    },
                    data: {
                        pageTitle: 'Хэрэглэгч'
                    }
                })

                .state("restricted.pages.organizations", {
                    url: "/organization",
                    templateUrl: 'app/custom/admin/system/pOrganizationView.html',
                    controller: 'orgNewCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/custom/admin/system/pOrganizationController.js'
                            ]);
                        }],
                        p_plan: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutPlan'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                    },
                    data: {
                        pageTitle: 'Байгууллага'
                    }
                })

                .state("restricted.pages.orglist", {
                    url: "/orglist",
                    templateUrl: 'app/custom/admin/system/pOrglistView.html',
                    controller: 'orglistCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/custom/admin/system/pOrglistController.js'
                            ]);
                        }],
                    },
                    data: {
                        pageTitle: 'Үйлчлүүлэгч Байгууллага'
                    }
                })
                .state("restricted.pages.zagwar", {
                    url: "/zagwar",
                    templateUrl: 'app/custom/pages/staus/zagwarFile/zagwarView.html',
                    controller: 'fileCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/custom/pages/staus/zagwarFile/zagwarController.js'
                            ]);
                        }]

                    },
                    data: {
                        pageTitle: 'Хэрэглэгч'
                    }
                })
                .state("restricted.pages.staus", {
                    url: "/staus",
                    templateUrl: 'app/custom/pages/staus/stausView.html',
                    controller: 'stausCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/custom/pages/staus/stausController.js'
                            ]);
                        }],
                        p_menu: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutMenu'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        user_data: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/user'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        }

                    },
                    data: {
                        pageTitle: 'Хэрэглэгч'
                    }
                })
                .state("restricted.pages.transactions", {
                    url: "/transactions",
                    templateUrl: 'app/custom/pages/journal/pPmenuView.html',
                    controller: 'transactionCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/custom/pages/journal/pPmenuController.js'
                            ]);
                        }],
                        p_menu: function ($http, $state, __env) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutMenu'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        user_data: function ($http, $state, __env) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/user'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        }

                    },
                    data: {
                        pageTitle: 'Хэрэглэгч'
                    }
                })

                .state("restricted.pages.forms", {
                    url: "/lutForm",
                    templateUrl: 'app/custom/admin/pFormView.html',
                    controller: 'lutFormCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'bower_components/angular-resource/angular-resource.min.js',
                                'lazy_datatables',
                                'app/custom/admin/pFormController.js'
                            ]);
                        }],
                        p_form: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutForm'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        user_data: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/user'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        }

                    },
                    data: {
                        pageTitle: 'Ажил'
                    }
                })

                .state("restricted.pages.pmenu", {
                    url: "/menu",
                    templateUrl: 'app/custom/admin/pPmenuView.html',
                    controller: 'menuCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/custom/admin/pPmenuController.js'
                            ]);
                        }],
                        p_menu: function ($http, $state, __env) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutMenu'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        user_data: function ($http, $state, __env) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/user'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        }

                    },
                    data: {
                        pageTitle: 'Цэс'
                    }
                })

                .state("restricted.pages.prole", {
                    url: "/role",
                    templateUrl: 'app/custom/admin/pProleView.html',
                    controller: 'roleCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/custom/admin/pProleController.js'
                            ]);
                        }],
                        p_menu: function ($http, $state, __env) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutMenu'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        user_data: function ($http, $state, __env) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/user'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        }
                    },
                    data: {
                        pageTitle: 'Эрх'
                    }
                })

                .state("restricted.pages.orgform", {
                    url: "/orgaddform/:param",
                    templateUrl: 'app/custom/pages/Organization/OrgFormView.html',
                    controller: 'orgformCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_wizard',
                                'lazy_KendoUI',
                                'lazy_ionRangeSlider',
                                'lazy_masked_inputs',
                                'lazy_character_counter',
                                'app/custom/pages/Organization/OrgFormController.js'
                            ]);
                        }],
                        p_dep: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutDepartment'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        p_fin: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutFincategory'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        p_cat: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutCategory'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        p_prog: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/LutExpProgcategory'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        p_tez: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/tez'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        p_aures: function ($http, $state) {
                            return $http({method: 'GET', url: __env.apiUrl() + '/core/resource/aures'})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        },
                        p_edit: function ($http, $state, $stateParams) {
                            return $http({
                                method: 'GET',
                                url: __env.apiUrl() + '/core/sel/editorg/' + $stateParams.param
                            })
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function (response) {
                                    $state.go("login_v2");
                                });
                        }

                    },
                    data: {
                        pageTitle: 'Байгууллага нэмэх'
                    }
                })


        }
    ]);
