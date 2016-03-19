var app = angular.module('app', [
    'ngResource',
    'resources',
    'ui.bootstrap',
    'indexControllers']);

app.constant("config", {"url": "http://localhost:8080"});