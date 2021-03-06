angular
    .module('altairApp')
    .controller('gallery_v2Ctrl', [
        '$scope',
        '$timeout',
        function ($scope,$timeout) {

            $scope.images = [
                {
                    'url': 'assets/img/gallery/Image01.jpg',
                    'title': 'Consequatur qui doloribus voluptas',
                    'date': '30 Jun 2016',
                    'size': '99KB',
                    'user_avatar': 'assets/img/avatars/avatar_07_tn.png',
                    'user_name': 'Hipolito Kuhic',
                    'comment': 'Nostrum accusamus laboriosam dicta nobis nisi omnis ut quia laboriosam dolorem accusamus aut molestiae corrupti dolor eum aut aperiam earum eum soluta natus corrupti et labore iure modi.'
                },
                {
                    'url': 'assets/img/gallery/Image02.jpg',
                    'title': 'Sit modi est earum voluptatem.',
                    'date': '15 Jun 2016',
                    'size': '31KB',
                    'user_avatar': 'assets/img/avatars/avatar_02_tn.png',
                    'user_name': 'Shad Kohler',
                    'comment': 'Laudantium explicabo consequuntur nobis possimus ducimus molestiae eum aliquam qui illo vero est ipsa assumenda et et quia facere autem a officiis.'
                },
                {
                    'url': 'assets/img/gallery/Image03.jpg',
                    'title': 'Est nemo eum',
                    'date': '01 Jun 2016',
                    'size': '28KB',
                    'user_avatar': 'assets/img/avatars/avatar_11_tn.png',
                    'user_name': 'Malcolm Grant',
                    'comment': 'Voluptatem et voluptate iure eaque consequatur culpa libero molestias rerum tempore accusantium ex reprehenderit accusantium.'
                },
                {
                    'url': 'assets/img/gallery/Image04.jpg',
                    'title': 'Dolores quae ea eum nulla',
                    'date': '19 Jun 2016',
                    'size': '54KB',
                    'user_avatar': 'assets/img/avatars/avatar_07_tn.png',
                    'user_name': 'Birdie Kautzer',
                    'comment': 'Placeat autem veniam sit voluptate nobis illo illo vero amet labore reprehenderit ut earum dolore aut non qui est dignissimos omnis provident pariatur adipisci.'
                },
                {
                    'url': 'assets/img/gallery/Image05.jpg',
                    'title': 'Impedit asperiores et placeat',
                    'date': '26 Jun 2016',
                    'size': '58KB',
                    'user_avatar': 'assets/img/avatars/avatar_07_tn.png',
                    'user_name': 'Myrtie Johns',
                    'comment': 'Eligendi hic non tenetur minus eaque accusamus repudiandae non assumenda corrupti facere eligendi saepe amet.'
                },
                {
                    'url': 'assets/img/gallery/Image06.jpg',
                    'title': 'Optio esse error est',
                    'date': '21 Jun 2016',
                    'size': '87KB',
                    'user_avatar': 'assets/img/avatars/avatar_06_tn.png',
                    'user_name': 'Athena Reichert',
                    'comment': 'Quam at est aut est et quaerat et dolorum sed in officiis rerum nihil maxime voluptate itaque deserunt soluta ut.'
                },
                {
                    'url': 'assets/img/gallery/Image07.jpg',
                    'title': 'Nobis dolor aut',
                    'date': '04 Jun 2016',
                    'size': '73KB',
                    'user_avatar': 'assets/img/avatars/avatar_07_tn.png',
                    'user_name': 'Alva Kuhic',
                    'comment': 'Non assumenda vel eaque maiores repudiandae aut repudiandae architecto qui distinctio placeat voluptas sit rerum ea culpa.'
                },
                {
                    'url': 'assets/img/gallery/Image08.jpg',
                    'title': 'Sequi placeat eos optio officiis',
                    'date': '01 Jun 2016',
                    'size': '88KB',
                    'user_avatar': 'assets/img/avatars/avatar_04_tn.png',
                    'user_name': 'Giovanna Kris',
                    'comment': 'Vitae veritatis asperiores doloremque velit ea voluptatem dicta animi sed cupiditate hic error beatae aliquid placeat reprehenderit aperiam sequi velit commodi.'
                },
                {
                    'url': 'assets/img/gallery/Image09.jpg',
                    'title': 'Quis et in vel sed',
                    'date': '27 Jun 2016',
                    'size': '31KB',
                    'user_avatar': 'assets/img/avatars/avatar_10_tn.png',
                    'user_name': 'Donnie Watsica',
                    'comment': 'Enim ipsam rerum ratione dolore laudantium pariatur dignissimos quis quisquam voluptas est possimus.'
                },
                {
                    'url': 'assets/img/gallery/Image10.jpg',
                    'title': 'Impedit neque voluptatem',
                    'date': '15 Jun 2016',
                    'size': '55KB',
                    'user_avatar': 'assets/img/avatars/avatar_04_tn.png',
                    'user_name': 'Zoey Hermiston',
                    'comment': 'Voluptate quidem fuga atque ut quidem ut quas harum pariatur neque fugiat recusandae tenetur consectetur eius eligendi necessitatibus consequatur occaecati qui.'
                },
                {
                    'url': 'assets/img/gallery/Image11.jpg',
                    'title': 'Doloremque et odio et eum ipsam',
                    'date': '23 Jun 2016',
                    'size': '44KB',
                    'user_avatar': 'assets/img/avatars/avatar_12_tn.png',
                    'user_name': 'Lamont Dooley',
                    'comment': 'Laboriosam veniam nesciunt impedit explicabo sit consequatur temporibus nesciunt porro amet saepe aut qui mollitia ea eaque dolorem ut aspernatur inventore voluptatem fugit et.'
                },
                {
                    'url': 'assets/img/gallery/Image12.jpg',
                    'title': 'Itaque tenetur quod voluptas',
                    'date': '13 Jun 2016',
                    'size': '104KB',
                    'user_avatar': 'assets/img/avatars/avatar_06_tn.png',
                    'user_name': 'Brett Wintheiser',
                    'comment': 'Consectetur voluptates enim qui aut quam et quis iusto ab ipsa doloribus ad qui omnis aut dignissimos dolorem labore doloremque et inventore molestias expedita.'
                }
            ];

            var $grid = $('#galleryGrid');

            $scope.$on('onLastRepeat', function (scope, element, attrs) {
                $timeout(function() {
                    UIkit.grid($grid, {
                        gutter: 16
                    });
                },500)
            });

            $scope.lightbox_content = null;
            var $customModal = $('#custom-ligthbox');
            if($customModal.length) {
                $scope.lightbox = UIkit.modal('#custom-ligthbox', {
                    center: true
                });

                $scope.lightboxOpen = function($event,index,last,first) {
                    $event.preventDefault();

                    var $this = $($event.currentTarget);
                    $this.addClass('jQ-lightbox-triggered');

                    $scope.lightbox_content = {
                        'img': $scope.images[index].url,
                        'user_avatar': $scope.images[index].user_avatar,
                        'user_name': $scope.images[index].user_name,
                        'comment': $scope.images[index].comment,
                        'first': first,
                        'last': last,
                        'index': index
                    };

                    $timeout(function () {
                        $scope.lightbox.show();
                    })
                };


                $scope.lightboxPrev = function($event) {
                    $event.preventDefault();
                    var $prev = $grid.find('.jQ-lightbox-triggered').closest('.md-card').parent('div').prev('div').find('.custom-modal-open');
                    if($prev.length) {
                        $scope.lightbox.hide();
                        $timeout(function() {
                            $prev.trigger('click')
                        }, 300)
                    }
                };

                $scope.lightboxNext = function($event) {
                    $event.preventDefault();
                    var $next = $grid.find('.jQ-lightbox-triggered').closest('.md-card').parent('div').next('div').find('.custom-modal-open');
                    if($next.length) {
                        $scope.lightbox.hide();
                        $timeout(function() {
                            $next.trigger('click')
                        }, 300)
                    }
                };

                $customModal.on({
                    'hide.uk.modal': function(){
                        $grid.find('.jQ-lightbox-triggered').removeClass('jQ-lightbox-triggered')
                    }
                });

            }

        }
    ]);