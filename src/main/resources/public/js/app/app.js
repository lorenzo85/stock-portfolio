var app = angular.module('app', [
    'ngResource',
    'toggle-switch',
    'ngMaterial',
    'ngAnimate',
    'ui.router',
    'ui.bootstrap']);


app.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/home');

    $stateProvider.state('home', {
        url: '/home',
        templateUrl: 'views/home.html',
        controller: 'HomeController'

    }).state('chart', {
        url: '/chart',
        templateUrl: 'views/chart.html',
        controller: 'ChartController'

    }).state('indexer', {
        url: '/indexer',
        templateUrl: 'views/indexer.html',
        controller: 'IndexerController'

    }).state('stock', {
        url: '/stock',
        templateUrl: 'views/stock.html',
        controller: 'StockController'
    })

});

app.constant("config", {
    "url": "http://localhost:8080"
});