var app = angular.module('app');

app.controller('IndexerController', function ($scope, Indexer) {

    $scope.title = 'Indexer API';

    // -- All stocks
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pageSize = 5;
    $scope.maxSize = 5;

    $scope.marketCodes = Indexer.get({size: $scope.pageSize, page: $scope.currentPage}, function(data) {
        $scope.totalItems = data.totalElements;
        $scope.totalPages = data.totalPages;
    });

    $scope.setPage = function (pageNo) {
        $scope.currentPage = pageNo;
    };

    $scope.pageChanged = function () {
        $scope.marketCodes = Indexer.get({size: $scope.pageSize, page: $scope.currentPage - 1});
    };


    // -- Suggester
    $scope.isDisabled = false;
    $scope.selectedTicker = undefined;

    $scope.querySearch = function (term) {
        var results = [];
        if (term) {
            results = Indexer.prefixSearch({term: term});
        }
        return results;
    };

    $scope.selectedItemChange = function (item) {
        $scope.selectedTicker = item;
    };

    // -- Reindex
    $scope.reindex = function () {
        Indexer.reindex();
    };

    // -- Suggester history
    $scope.h_isDisabled = false;
    $scope.h_selectedTicker = undefined;

    $scope.h_querySearch = function (term) {
        var results = [];
        if (term) {
            results = Indexer.prefixHistorySearch({term: term});
        }
        return results;
    };

    $scope.h_selectedItemChange = function (item) {
        $scope.h_selectedTicker = item;
    };


});