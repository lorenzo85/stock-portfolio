
var app = angular.module('navigationController', []);

app.controller('NavigationController', function ($scope) {

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