var app = angular.module('app');

app.factory('Stock', function ($resource, config) {

    return $resource(config.url + '/update/market/codes', {}, {

        updateMarketCodes: {
            url: config.url + '/update/market/codes',
            method: 'GET',
            params: {marketid: '@marketid'}
        },

        updateSingleHistoryCode: {
            url: config.url + '/update/history/code',
            method: 'GET',
            params: {marketid: '@marketid', code: '@code'}
        },

        updateHistoryCode: {
            url: config.url + '/update/history/code/all',
            method: 'GET'
        }
    });
});