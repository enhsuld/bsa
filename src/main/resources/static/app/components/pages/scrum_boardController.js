angular
    .module('altairApp', [ angularDragula(angular) ] )
    .controller('scrum_boardCtrl', [
        '$rootScope',
        '$scope',
        'tasks_list',
        'dragulaService',
        function ($rootScope,$scope,tasks_list,dragulaService) {

            $rootScope.page_full_height = true;

            $scope.$on('$destroy', function() {
                $rootScope.page_full_height = false;
            });

            $scope.$on('onLastRepeat', function (scope, element, attrs) {
                // set width for scrum board container
                var $scrumBoard = $('#scrum_board'),
                    childWidth = $scrumBoard.children('div').width(),
                    childsCount = $scrumBoard.children('div').length;

                $scrumBoard.width(childWidth * childsCount);
            });

            $scope.task_groups = [
                {
                    id: 'todo',
                    name: 'To Do'
                },
                {
                    id: 'inAnalysis',
                    name: 'In analysis'
                },
                {
                    id: 'inProgress',
                    name: 'In progress'
                },
                {
                    id: 'done',
                    name: 'Done'
                }
            ];

            $scope.tasks_list = tasks_list;

            $scope.tasks_common = [
                {
                    "id": 0,
                    "name": "Altair-366",
                    "title": "Et voluptatem ea vel.",
                    "description": "Aut unde id perferendis distinctio alias omnis iusto.",
                    "status": "critical",
                    "assignee": "Dangelo Purdy"
                },
                {
                    "id": 1,
                    "name": "Altair-532",
                    "title": "Exercitationem placeat qui numquam.",
                    "description": "Sit voluptatum officia consequatur architecto quos explicabo.",
                    "status": "minor",
                    "assignee": "Brooks Dickens"
                },
                {
                    "id": 2,
                    "name": "Altair-8235",
                    "title": "Odit sit possimus.",
                    "description": "Laboriosam aut quasi ipsam harum ex animi.",
                    "status": "blocker",
                    "assignee": "Leone Bode"
                },
                {
                    "id": 3,
                    "name": "Altair-2548",
                    "title": "Qui aliquid dicta possimus.",
                    "description": "Nihil rerum ipsum et animi occaecati harum provident quia.",
                    "status": "minor",
                    "assignee": "Jana DuBuque"
                }
            ];

            // task info
            $scope.taskInfo = function(task) {
                $scope.info = {
                    name: task.name,
                    title: task.title,
                    status: task.status,
                    description: task.description,
                    assignee: task.assignee
                }
            };

            // new task
            $scope.newTask = {
                name: 'Altair-245',
                assignee: [
                    { id: 1, title: 'Aleen Grant' },
                    { id: 2, title: 'Tyrese Koss' },
                    { id: 3, title: 'Chasity Green' },
                    { id: 4, title: 'Me' }
                ],
                group: [
                    { name: 'todo', title: 'To Do' },
                    { name: 'inAnalysis', title: 'In Analysis' },
                    { name: 'inProgress', title: 'In Progress' },
                    { name: 'done', title: 'Done' }
                ]
            };

            $scope.newTask_assignee =  $scope.newTask.assignee;
            $scope.newTask_assignee_config = {
                create:false,
                maxItems: 1,
                valueField: 'id',
                labelField: 'title',
                placeholder: 'Select Assignee...'
            };
            $scope.newTask_group =  $scope.newTask.group;
            $scope.newTask_group_config =  {
                create:false,
                maxItems: 1,
                valueField: 'name',
                labelField: 'title',
                placeholder: 'Select Group...'
            };

            $scope.$on('tasks.drop', function (e, el, target, source) {
                console.log(target[0].id);
            });

        }
    ]);