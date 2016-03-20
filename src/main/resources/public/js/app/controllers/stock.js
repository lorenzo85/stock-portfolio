var app = angular.module('app');

app.controller('StockController', function ($scope, Stock) {

    $scope.title = 'Stock API';
    $scope.marketId = 'LSE';
    $scope.code = 'APT';

    $scope.updateMarketCodes = function () {
        Stock.updateMarketCodes({marketid: $scope.marketId});
    };

    $scope.updateSingleHistoryCode = function () {
        Stock.updateSingleHistoryCode({marketid: $scope.marketId, code: $scope.code});
    };

    $scope.updateHistoryCode = function () {
        Stock.updateHistoryCode();
    };

});