var app = angular.module('app', [
    'ngResource',
    'toggle-switch',
    'resources',
    'ngMaterial',
    'ngAnimate',
    'ui.router',
    'ui.bootstrap',
    'navigationController',
    'homeController']);

app.constant("config", {
    "url": "http://localhost:8080"
});


app.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/home');

    $stateProvider.state('home', {
        url: '/home',
        templateUrl: 'views/home.html',
        controller: 'IndexController'

    }).state('navigation', {
        templateUrl: 'views/navigation.html',
        controller: 'NavigationController'
    });

});