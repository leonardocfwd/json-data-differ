angular.module('jsonDiffApp')                                                                                                                                                                        
   .factory("appUtils", function() {                                                                                                                                                   
	     return {                                                                                                                                                                                                              
	    	 launchModal : function(message){
	    		 $('#modalMessage').text(message);
	    		 $('#myModal').modal('show');
	    	 }
	    }
});