'use strict';


angular.module('app')
	.directive('headerNotification',function(){
		return {
        templateUrl:'js/app/directives/header/header-notification/header-notification.html',
        restrict: 'E',
        replace: true
    	}
	});


