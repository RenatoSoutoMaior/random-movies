(function () {
    'use strict';

    angular
        .module('app')
        .controller('MoviesController', MoviesController);

    MoviesController.$inject = ['$http'];

    function MoviesController($http) {
        var vm = this;

        vm.movies = [];

        vm.getTopRatedMovie = getTopRatedMovie;
        vm.getRandomMovie = getRandomMovie;

        init();

        function init() {
            getTopRatedMovie();
        }

        function getTopRatedMovie() {
            $http({
                method: 'GET',
                url: '/movies/top-rated'
            }).then(function successCallback(response) {
                vm.movies = response.data;
                console.log(vm.movies);
            }, function errorCallback(response) {
            });
        }

        function getRandomMovie() {
            $http({
                method: 'GET',
                url: '/movies/random-movie'
            }).then(function successCallback(response) {
                vm.movies = response.data;
                console.log(vm.movies);
            }, function errorCallback(response) {
            });
        }
    }
})();