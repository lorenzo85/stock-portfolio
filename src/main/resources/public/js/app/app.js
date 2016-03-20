var app = angular.module('app', [
    'ngResource',
    'resources',
    'ngMaterial',
    'ui.bootstrap',
    'indexControllers']);

app.constant("config", {"url": "http://localhost:8080"});