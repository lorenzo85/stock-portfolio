var app = angular.module('indexControllers', []);

app.controller('IndexController', function ($scope) {

    $scope.markets = [
        {
            id: 'LSE',
            description: 'London Stock Exchange'
        },
        {
            id: 'SSE',
            description: 'Singapore Stock Exchange'
        },
        {
            id: 'USSE',
            description: 'US Stock Exchange'
        }
    ];
    $scope.marketCodes = function () {
        return $scope.markets;
    }

});