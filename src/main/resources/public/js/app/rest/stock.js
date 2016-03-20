var app = angular.module('resources', []);

app.factory('StockCodes', function($resource, config) {
    return $resource(config.url + '/query/stock/codes/:size/:page', {size: '@size', page: '@page'}, {
       prefixSearch: {
           url: config.url + '/query/stock/codes/search/:term',
           method: 'GET',
           params: {term: '@term'},
           isArray: true
       }
    });
});