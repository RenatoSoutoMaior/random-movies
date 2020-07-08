(function () {
    'use strict';

    angular
        .module('app')
        .controller('MoviesController', MoviesController);

    MoviesController.$inject = ['$http'];

    function MoviesController($http) {
        var vm = this;

        vm.movies = [];
        vm.getTopRated = getTopRated;

        init();

        function init() {
            getTopRated();
        }

        function getTopRated() {
            $http({
                method: 'GET',
                url: '/movies/top-rated'
            }).then(function successCallback(response) {
                vm.movies = response.data;
                console.log(vm.movies);
            }, function errorCallback(response) {
            });
        }
    }
})();