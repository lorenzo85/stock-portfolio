var app = angular.module('indexControllers', []);

app.controller('IndexController', function ($scope, StockCodes) {

    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pageSize = 5;
    $scope.maxSize = 5;

    $scope.searchResults = [];


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

    $scope.search = function(term) {
        // Term is empty
        if (!term) {
            $scope.searchResults = [];
        } else {
            $scope.searchResults = StockCodes.prefixSearch({term: term});
        }
    }

});