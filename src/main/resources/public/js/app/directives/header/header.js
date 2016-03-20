'use strict';


angular.module('app')
	.directive('header',function(){
		return {
        templateUrl:'js/app/directives/header/header.html',
        restrict: 'E',
        replace: true

    	}
	});


