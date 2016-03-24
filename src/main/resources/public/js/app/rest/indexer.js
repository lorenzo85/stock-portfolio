var app = angular.module('app');

app.factory('Indexer', function ($resource, config) {

    return $resource(config.url + '/query/stock/codes/:size/:page', {size: '@size', page: '@page'}, {

        prefixSearch: {
            url: config.url + '/query/stock/codes/search/:term',
            method: 'GET',
            params: {term: '@term'},
            isArray: true
        },

        prefixHistorySearch: {
            url: config.url + '/query/stock/codes/history/search/:term',
            method: 'GET',
            params: {term: '@term'},
            isArray: true
        },

        totalCodes: {
            url: config.url + '/query/stock/codes/total',
            method: 'GET'
        },

        totalCodesHistory: {
            url: config.url + '/query/stock/codes/history/total',
            method: 'GET'
        }
    });
});