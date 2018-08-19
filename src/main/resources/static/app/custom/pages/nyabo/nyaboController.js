angular
.module('altairApp')
	.controller("nyaboCtrl",['$rootScope','$scope','$timeout','user_data','mainService','sweet','$state','Upload','fileUpload','__env',
		function ($rootScope,$scope,$timeout,user_data,mainService,sweet,$state,Upload,fileUpload,__env) {
			$rootScope.fullHeaderActive = true;
			
			$scope.$on('$destroy', function() {
				$rootScope.fullHeaderActive = false;
			});
		
		$('.dropify').dropify();
	
		
		$scope.formUpload={};
		$scope.formFile={};
		$scope.submitUploadNotlohZuil = function() {
		   $scope.sendBtn=false;
		   if ($scope.formUpload.uploadfile.$valid && $scope.uploadfile) {
				bar.css("width", "0%").text("0%");
			  progressbar.removeClass("uk-hidden");
			  $scope.uploadNotlohZuil($scope.uploadfile,2);
		   }
		};
		
	 
		$scope.submitUploadDifference = function() {
		   $scope.sendBtn=false;
		   if ($scope.formFile.afl.$valid && $scope.afl) {
			   bar.css("width", "0%").text("0%");
			   progressbar.removeClass("uk-hidden");
			   $scope.uploadNotlohZuil($scope.afl,1);
			}
		};
		
		
		$scope.download = function(id){
			mainService.withdomain('get', __env.apiUrl()+'/api/file/download/'+id).
			then(function(data){
				
			});  
		  }
		
		$scope.change=function(){	        	
			 $scope.diff=[];
		}

		$scope.pmenuGrid = {
			dataSource: {	                   
				transport: {
					read:  {
						url: __env.apiUrl()+"/core/list/FileConverted",
						contentType:"application/json; charset=UTF-8",     
						data: {"custom":"where userid="+user_data.id,"sort":[{field: 'id', dir: 'desc'}]},
						type:"POST"
					},
					update: {
						url: __env.apiUrl()+"/core/update/"+$scope.domain+"",
						contentType:"application/json; charset=UTF-8",                                    
						type:"POST",
						complete: function(e) {
							$(".k-grid").data("kendoGrid").dataSource.read(); 
						}
					},
					destroy: {
						url: __env.apiUrl()+"/core/delete/"+$scope.domain+"",
						contentType:"application/json; charset=UTF-8",                                    
						type:"POST"
					},
					create: {
						url: __env.apiUrl()+"/core/create/"+$scope.domain+"",
						contentType:"application/json; charset=UTF-8",                                    
						type:"POST",
						complete: function(e) {
							$(".k-grid").data("kendoGrid").dataSource.read(); 
						}
					},
					parameterMap: function(options) {
						return JSON.stringify(options);
					}
				},
				schema: {
						data:"data",
						total:"total",
						model: {                                	
							id: "id",
							fields: {   
								id: { editable: false,nullable: true},
								menuname: { type: "string", validation: { required: true } },
								stateurl: { type: "string", defaultValue:'#'},
							uicon: { type: "string"},
							parentid: { type: "number"},
							orderid: { type: "number" }
							}
						}
					},
				pageSize: 8,
				serverFiltering: true,
				serverPaging: true,
				serverSorting: true
			},
			filterable:{
						mode: "row"
				},
			toolbar: kendo.template($("#add").html()),
			height: function(){
				return $(window).height()-180;
			},
			sortable: true,
			resizable: true,
			pageable: {
				refresh: true,
				pageSizes: true,
				buttonCount: 5
			},
			columns: [
					{title: "#",template: "<span class='row-number'></span>", width:"60px"},
					{ field:"name", title: "Тайлан" },
					{ field: "fdate",  title:"Огноо"},
					{ field: "fsize", title:"Хэмжээ"},
					{template: kendo.template($("#download").html()), width: "200px"}
			],
			dataBound: function () {
				var rows = this.items();
					$(rows).each(function () {
						var index = $(this).index() + 1 
						+ ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
						var rowLabel = $(this).find(".row-number");
						$(rowLabel).html(index);
					});
					},
					
		};  
		$scope.diff=[];
		$scope.userId=user_data.id;
		
		$scope.tulgalt= function(dataItem){
			var tval=[{"text":"ROOT","value":"0"},
			{"text":"Санхүүгийн байдлын тайлан дахь үзүүлэлтийн тулгалт буюу уялдаа","value":"1"},
			{"text":"Санхүүгийн үр дүнгийн тайлан дахь үзүүлэлтийн тулгалт буюу уялдаа","value":"2"},
			{"text":"Мөнгөн гүйлгээний тайлан дахь үзүүлэлтийн тулгалт буюу уялдаа","value":"3"},
			{"text":"Санхүүгийн тайлан хоорондын тулгалт буюу уялдаа","value":"4"},
			{"text":"Санхүүгийн тайлан, тодруулга хоорондын тулгалт буюу уялдаа","value":"5"}];
			var balance=[{"text":"Эхний үлдэгдэл","value":"1"},
			{"text":"Эцсийн үлдэгдэл","value":"2"},
			{"text":"Өмнөх оны гүйцэтгэл ","value":"21"},
			{"text":"Тайлант оны гүйцэтгэл","value":"22"}];
			var modalNotloh = UIkit.modal("#modal_zuruu", {modal: false, keyboard: false, bgclose: false, center: false});
			modalNotloh.show();
			$scope.mainGridOptions = {
				dataSource: {    	                   
					transport: {
						read:  {
							url: __env.apiUrl()+"/core/list/StsCheckVariable",
							contentType:"application/json; charset=UTF-8",                                    
							type:"POST",
							data:{"custom":"where planid = " + dataItem.id +""},
						},
						parameterMap: function(options) {
							return JSON.stringify(options);
					   }
					},
					schema: {
						 data:"data",
						 total:"total",
						 model: {                                	
							 id: "id",
							 fields: {   
								 id: { type: "number", editable: false,nullable: false},         
								 data1: { type: "string", editable: false},
								 data2: { type: "string", editable: false},
								 data3: { type: "string", editable: false},
								 data4: { type: "string", editable: false},
								 data5: { type: "string", editable: false},
								data6: { type: "number", editable: false},
								 data7: { type: "string", editable: false},
								data8: { type: "string", editable: false},
								data9: { type: "number", editable: false},
								data10: { type: "number", editable: false}
							 }	                    
						 }
					 },
					 group: {
						 field: "valid",values:tval
					   },
					pageSize: 14,
					serverPaging: true,
					serverFiltering: true,
					serverSorting: true,
					sort: { field: "id", dir: "asc" }
				},
				filterable: {
					mode:"row"
				},
				sortable: true,
				resizable: true,
				toolbar:["excel"],
				pageable: {
					refresh: true,
					pageSizes: true,
					buttonCount: 5
				},
				excel: {
					   fileName: "Tulgalt Export.xlsx",
					   proxyURL: "//demos.telerik.com/kendo-ui/service/export",
					   filterable: true,
					   allPages: true
				   },
				columns: [
						  { field: "valid", title: "Тулгалт" +"<span data-translate=''></span>",values:tval,width:"300px"},
						  { title: "Харьцуулсан үзүүлэлт", columns:[{field:"data1", title:"1"},{field:"data2", title:"2"}]},
						  { title: "Үлдэгдэл", columns:[{field:"data3", title:"3",values:balance}]},
						  { title: "Дансны код-А", columns:[{field:"data4", title:"4"}]}, 
						  { title: "Дансны код-Б", columns:[{field:"data7", title:"7"}]},   
						  { title: "Дүн-А", columns:[{field:"data6", title:"8", format: "{0:n}"}]},    	                          	                         
						  { title: "Дүн-Б", columns:[{field:"data9", title:"9", format: "{0:n}"}]},
						  { title: "Зөрүү", columns:[{field:"data10", format: "{0:n}", title:"10", template:"# if (data10 != 0) { # <span class='tulgaltRed'>#=kendo.format('{0:n}', data10)#</span> # } else { # #=kendo.format('{0:n}', data10)# # } #"}]},

						  ],
					  editable: $scope.editable,
					  dataBound: function(){
						  $(".tulgaltRed").parent().addClass("md-bg-red-100");
						},
		  };
		}
			
		$scope.zuruuGrid=false;
		$scope.uploadNotlohZuil = function (file,y) {
			  var xurl="";
			  if(y==1){
				  xurl=__env.apiUrl()+'/api/checker';
			  }		
			  else{
				  xurl=__env.apiUrl()+'/api/nyabo';
			  }
			  Upload.upload({
				  url: xurl,
				  data: {file: file, 'description': $scope.description}
			  }).then(function (resp) {
				  console.log(resp);
				  if(resp){
					  sweet.show('Анхаар!', 'Тулгалт товч дээр дарж зөрүүгээ шалгана уу!!!', 'success');
				  }
				  progressbar.removeClass("uk-hidden");
				  console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
				  $scope.ars=resp.data.error;
				  $scope.uploadfile=null;
				  $scope.uploadfileDif=null;		         
				  //$(".nnn .k-grid").data("kendoGrid").dataSource.read(); 
				  progressbar.addClass("uk-hidden");
			  }, function (resp) {
				  console.log('Error status: ' + resp.status);
			  }, function (evt) {
				  var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
				 
				  percent = progressPercentage;
				 bar.css("width", percent+"%").text(percent+"%");                    
				  console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
			  });
			  
			
		   };
	
		   

		
		
		  var progressbar = $("#file_upload-progressbar"),
		  bar         = progressbar.find('.uk-progress-bar'),
		  settings    = {

			  action: __env.apiUrl()+'/api/nyabo', // upload url

			  allow : '*.(xlsx|xls)', // allow only images

			  loadstart: function() {
				  bar.css("width", "0%").text("0%");
				  progressbar.removeClass("uk-hidden");
			  },

			  progress: function(percent) {
				  percent = Math.ceil(percent);
				  bar.css("width", percent+"%").text(percent+"%");
			  },

			  allcomplete: function(response) {

				  bar.css("width", "100%").text("100%");

				  setTimeout(function(){
					  progressbar.addClass("uk-hidden");
				  }, 250);

				  console.log(response);
				  alert("Upload Completed")
			  }
		  };

		  var select = UIkit.uploadSelect($("#file_upload-select"), settings),
		  drop   = UIkit.uploadDrop($("#file_upload-drop"), settings);
		
		  
		  
			$scope.downloadExcel=function(){
				if(user_data.plan!="basic"){
				  $rootScope.content_preloader_show();
					mainService.withdomain('get',__env.apiUrl()+'/api/file/verify/excel').then(function(response){
						 if(response.excel){
							 var link = document.createElement('a');
						  link.href = __env.apiUrl()+'/api/file/download/excel';
						  link.download = "Filename";
						  link.click();	
						  setTimeout(function(){
								  $rootScope.content_preloader_hide();
							 }, 4000);
						 
						 }
						 else if(!response.balance){
							sweet.show('Анхаар!', 'Багцын эрх дууссан байна!!!', 'error');
						 }
						 else{
							 sweet.show('Анхаар!', 'Excel тайлан олдсонгүй!!!', 'error');
							 $rootScope.content_preloader_hide();
						 } 	           		
					 });
				}
				else{
					sweet.show('Анхаар!', 'Багцаа дээшлүүлнэ үү!!!', 'error');
				} 		   	 	  	   	 	
			   }
		
		  $("#spreadSheetZagwarView").kendoSpreadsheet();
		  $scope.viewExcel =function(){
				 $rootScope.content_preloader_show();
				   mainService.withdomain('get',__env.apiUrl()+'/api/file/verify/excel/').then(function(response){
						if(response!=false){
						   $scope.xlsx=true;
						 $scope.purl=__env.apiUrl()+'/api/file/sheet/excel';
						 var xhr = new XMLHttpRequest();
						   xhr.open('GET',  $scope.purl, true);
						   xhr.responseType = 'blob';
							
						   xhr.onload = function(e) {
							 if (this.status == 200) {
							   var blob = this.response;
							   
							   console.log(blob);
							   var spreadsheet = $("#spreadSheetZagwarView").data("kendoSpreadsheet");
								 spreadsheet.fromFile(blob);				 		          
								   UIkit.modal("#modal_excel_file", {center: false}).show();
							 }
							 else{
								 sweet.show('Анхаар!', 'Файл устгагдсан байна.', 'error');
							 }
						   };
						setTimeout(function(){
							 $rootScope.content_preloader_hide();
						 }, 3000);
						   xhr.send();    	 					
						}
						else{
							sweet.show('Анхаар!', 'Excel тайлан оруулаагүй байна !!!', 'error');
							$rootScope.content_preloader_hide();
						}
					   
					});
			}        
		
		 // $scope.viewExcel();
		}
]);
