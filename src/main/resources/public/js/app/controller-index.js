var app = angular.module('indexControllers', []);

app.controller('IndexController', function ($scope, StockCodes) {

    // -- Pagination specific methods
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pageSize = 5;
    $scope.maxSize = 5;

    $scope.marketCodes = StockCodes.get({size: $scope.pageSize, page: $scope.currentPage}, function(data) {
        $scope.totalItems = data.totalElements;
        $scope.totalPages = data.totalPages;
    });

    $scope.setPage = function (pageNo) {
        $scope.currentPage = pageNo;
    };

    $scope.pageChanged = function() {
        $scope.marketCodes = StockCodes.get({size: $scope.pageSize, page: $scope.currentPage - 1});
    };


    // -- Autocomplete specific methods
    $scope.isDisabled    = false;
    $scope.selectedTicker = undefined;

    $scope.querySearch = function(term) {
        var results = [];
        if (term) {
            results = StockCodes.prefixSearch({term: term});
        }
        return results;
    };

    $scope.selectedItemChange = function(item) {
        $scope.selectedTicker = item;
    };

});