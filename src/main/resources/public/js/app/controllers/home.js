var app = angular.module('app');

app.controller('HomeController', function ($scope, Indexer) {

    $scope.totalCodes = Indexer.totalCodes();


});