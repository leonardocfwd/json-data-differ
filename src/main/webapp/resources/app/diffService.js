angular.module('jsonDiffApp').service('diffService', function($http, $location, $q, $base64, appUtils) {
	var self = this;
	
	self.sendFile = function(file, endpoint, diffId){
		var defer = $q.defer();
		if(file.size > 50000 * 100){
			appUtils.launchModal('File cannot exceed 5MB. Please try a different one.');
			return false;
		}
		var r = new FileReader();
		r.onload = function(){
			self.onFileReady(r.result, endpoint, diffId).then(function(responseMessage){
				defer.resolve(responseMessage);
			}); 
		};
		r.readAsBinaryString(file);
		
		return defer.promise;
	}
	
	self.onFileReady = function(file, endpoint, diffId){
		var defer = $q.defer();
		var data = $base64.encode(file);
		
		$http.post('v1/diff/'+diffId+'/'+endpoint,data,{
			transformRequest: function(data, headersGetterFunction) {
				return data;
			},
			headers: {'Content-Type': 'application/json'}
		}).then(function(response) {
			defer.resolve(response.data.message);
		}, function() {
			appUtils.launchModal('Application error. Your file could not be uploaded.');
		});
		
		return defer.promise;
	}

	self.getDiffResults = function(diffId){
		var defer = $q.defer();

		$http.get('v1/diff/'+diffId)
		.then(function(response) {
			defer.resolve(response.data);
		}, function() {
			defer.reject(response);
		});

		return defer.promise;
	}
});