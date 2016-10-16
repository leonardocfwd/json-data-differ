angular.module('jsonDiffApp').controller('diffController', function($http, $rootScope ,$scope, $location, $window, diffService, appUtils) {
	var self = this;
	self.diffId;
	self.jsonReturn;

	self.sendLeftFile = function(){
		if(!self.validateDiffId()){
			return;
		}
		var file = $scope.myLeftFile;
		if(!file){
			appUtils.launchModal('Please select the file first.');
			return ;
		}
		diffService.sendFile(file, 'left', self.diffId).then(function(responseMessage){
			appUtils.launchModal(responseMessage);
		});
	}
	
	self.sendRightFile = function(){
		if(!self.validateDiffId()){
			return;
		}
		var file = $scope.myRightFile;
		if(!file){
			appUtils.launchModal('Please select the file first.');
			return ;
		}
		diffService.sendFile(file, 'right', self.diffId).then(function(responseMessage){
			appUtils.launchModal(responseMessage);
		});
	}
	
	self.getResults = function(){
		if(!self.validateDiffId()){
			return;
		}
		diffService.getDiffResults(self.diffId).then(function(responseObject){
			self.jsonReturn = JSON.stringify(responseObject);
			appUtils.launchModal(self.jsonReturn);
		});
	}
	
	self.validateDiffId = function(){
		if(!self.diffId){
			appUtils.launchModal('Please provide the Diff id.');
			return false;
		}
		return true;
	}

});