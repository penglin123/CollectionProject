var isloading = true;
var app = angular.module('waitVisitTask', ['ionic']);
app.controller('waitVisitTask', ['$scope', '$timeout', '$http', '$ionicPopup', '$ionicPopover',
	function($scope, $timeout, $http, $ionicPopup, $ionicPopover) {

		$scope.ion_content = true;
		$scope.load_error = true;
		//		$scope.loading = false;

		$timeout(function() {
			$scope.ion_content = false;
			$scope.loading = true;
			//			$scope.load_error=false;
		}, 100);

		$scope.items = [{
			"name": "一号",
			"qiankuang": "89499965",
			"yuqitianshu": "527",
			"daojishi": "4",
			"jiedanshijian": "04月08日",
			"jiatingdizhi": "上海市中科路2200号上海市中科路2200号上海市中科路2200号上海市中科路2200号上海市中科路2200号上海市中科路2200号"
		}, {
			"name": "二号",
			"qiankuang": "27687",
			"yuqitianshu": "174",
			"daojishi": "9",
			"jiedanshijian": "04月08日",
			"jiatingdizhi": "上海市中科路2211号"
		}, {
			"name": "三号",
			"jiedanshijian": "04月08日",
			"qiankuang": "5457654",
			"yuqitianshu": "54",
			"daojishi": "7",
			"jiatingdizhi": "上海市中科路2233号"
		}, {
			"name": "四号",
			"jiedanshijian": "04月08日",
			"qiankuang": "46456",
			"yuqitianshu": "524",
			"daojishi": "7",
			"jiatingdizhi": "上海市中科路2255号"
		}, {
			"name": "五号",
			"qiankuang": "767967",
			"jiedanshijian": "04月08日",
			"yuqitianshu": "5463",
			"daojishi": "1",
			"jiatingdizhi": "上海市中科路2277号"
		}];

		$scope.doRefresh = function() {

			$http.get('') //注意改为自己本站的地址，不然会有跨域问题
				.success(function(newItems) {

				})
				.finally(function() {
					newItems = [{
						"name": "一号",
						"qiankuang": "89465",
						"jiedanshijian": "08月08日",
						"yuqitianshu": "527",
						"daojishi": "4",
						"jiatingdizhi": "上海市中科路2200号"
					}, {
						"name": "二号",
						"qiankuang": "27687",
						"jiedanshijian": "10月08日",
						"yuqitianshu": "174",
						"daojishi": "9",
						"jiatingdizhi": "上海市中科路2211号"
					}, {
						"name": "三号",
						"qiankuang": "5457654",
						"jiedanshijian": "09月08日",
						"yuqitianshu": "54",
						"daojishi": "7",
						"jiatingdizhi": "上海市中科路2233号"
					}, {
						"name": "四号",
						"qiankuang": "46456",
						"yuqitianshu": "524",
						"jiedanshijian": "01月20日",
						"daojishi": "7",
						"jiatingdizhi": "上海市中科路2255号"
					}, {
						"name": "五号",
						"qiankuang": "767967",
						"yuqitianshu": "5463",
						"jiedanshijian": "04月03日",
						"daojishi": "1",
						"jiatingdizhi": "上海市中科路2277号"
					}];
					$scope.items = newItems;
					$scope.$broadcast('scroll.refreshComplete');
				});
		};

		/*加载*/
		$scope.loadMore = function() {
			if(isloading) {
				$http.get('')
					.success(function(newItems) {

					})
					.finally(function() {
						$scope.expression = false;
						newItems = [{
							"name": "一号",
							"qiankuang": "89465",
							"yuqitianshu": "527",
							"jiedanshijian": "01月11日",
							"daojishi": "4",
							"jiatingdizhi": "上海市中科路2200号"
						}, {
							"name": "二号",
							"qiankuang": "27687",
							"yuqitianshu": "174",
							"jiedanshijian": "04月08日",
							"daojishi": "9",
							"jiatingdizhi": "上海市中科路2211号"
						}, {
							"name": "三号",
							"qiankuang": "5457654",
							"jiedanshijian": "03月28日",
							"yuqitianshu": "54",
							"daojishi": "7",
							"jiatingdizhi": "上海市中科路2233号"
						}, {
							"name": "四号",
							"qiankuang": "46456",
							"jiedanshijian": "02月08日",
							"yuqitianshu": "524",
							"daojishi": "7",
							"jiatingdizhi": "上海市中科路2255号"
						}, {
							"name": "五号",
							"qiankuang": "767967",
							"yuqitianshu": "5463",
							"jiedanshijian": "04月18日",
							"daojishi": "1",
							"jiatingdizhi": "上海市中科路2277号"
						}];

						for(var i = 0; i < newItems.length; i++) {
							$scope.items.push(newItems[i])
						}
						$scope.$broadcast('scroll.infiniteScrollComplete');;
					});
			} else {
				$scope.$broadcast('scroll.infiniteScrollComplete');
			}

		};

		$scope.moreDataCanBeLoaded = function() {
			return isloading = $scope.items.length > 10 ? false : true;
		};

		$scope.details = function(item) {

			//JS调用注册方法 jsTojava【JS代码】
			window.WebViewJavascriptBridge.callHandler(
				'jsTojava', {
					'data': item.name,
					'tag': 0
				},
				function(responseData) {
					//document.getElementById("show").innerHTML = "send get responseData from java, data = " + responseData
				}
			);

		}
		$scope.pathPlanning = function(item) {
			//JS调用注册方法 jsTojava【JS代码】
			window.WebViewJavascriptBridge.callHandler(
				'jsTojava', {
					'data': item.jiatingdizhi,
					'tag': 1
				},
				function(responseData) {
					//document.getElementById("show").innerHTML = "send get responseData from java, data = " + responseData
				}
			);
		}
		$scope.writeSummary = function(item) {
			//JS调用注册方法 jsTojava【JS代码】
			window.WebViewJavascriptBridge.callHandler(
				'jsTojava', {
					'data': item.name,
					'tag': 2
				},
				function(responseData) {
					//document.getElementById("show").innerHTML = "send get responseData from java, data = " + responseData
				}
			);
		}

				$scope.statementOfAccount = function(item) {
        			//JS调用注册方法 jsTojava【JS代码】
        			window.WebViewJavascriptBridge.callHandler(
        				'jsTojava', {
        					'data': item.name,
        					'tag': 3
        				},
        				function(responseData) {
        					//document.getElementById("show").innerHTML = "send get responseData from java, data = " + responseData
        				}
        			);
        		}
	}
])