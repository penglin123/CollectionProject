var isloading = true;
var app = angular.module('statistics', ['ionic']);
app.controller('statistics', ['$scope', '$timeout', '$http', '$ionicLoading',
	function($scope, $timeout, $http, $ionicLoading) {

		var myDate = new Date();
		var myMonth = myDate.getMonth() + 1;
		var myYear = myDate.getFullYear();

		var loadstatus = function(tag) {
			switch(tag) {
				case 0: //加载中
					$scope.ion_content = true;
					$scope.loading = false;
					$scope.load_error = true;
					break;
				case 1: //加载失败
					$scope.ion_content = true;
					$scope.loading = true;
					$scope.load_error = false;
					break;
				case 2: //加载成功
					$scope.ion_content = false;
					$scope.loading = true;
					$scope.load_error = true;
			}
		}

		//网络请求
		var http = function(year, month) {
			$ionicLoading.show({
				noBackdrop: true,
				animation: 'fade-in',
				showBackdrop: true,
				Width: 100,
				showDelay: 0
			});

			$http.get('http://gank.io/api/data/福利/30/' + month)
				.success(function(newItems) {
				var	monthStr = month >= 10 ? month : "0" + month;
					$scope.time = year + "年" + monthStr + "月";
					myMonth = month;
					myYear = year;
					$scope.items = newItems.results;
				})
				.finally(function() {
					$ionicLoading.hide();
				});
		};

		loadstatus(0);
		$http.get('http://gank.io/api/data/福利/30/' + myMonth)
			.success(function(newItems) {
				loadstatus(2);
			
				var	monthStr = myMonth >= 10 ? myMonth : "0" + myMonth;
		
				$scope.time = myYear + "年" + monthStr + "月";
				$scope.items = newItems.results;
			})
			.finally(function() { 
				loadstatus(1);
			});

		$scope.againLoad = function() {
			loadstatus(0);
			$http.get('http://gank.io/api/data/福利/30/' + myMonth)
				.success(function(newItems) {
					loadstatus(2);
					
					var	monthStr = myMonth >= 10 ? myMonth : "0" + myMonth;
		
				$scope.time = myYear + "年" + monthStr + "月";
					$scope.items = newItems.results;
				})
				.error(function(err) {

					loadstatus(1);
				});
		};
		$scope.previous = function() {
			if(myMonth == 1) {
				myYear--;
				myMonth = 12;
			} else {
				myMonth--;
			}

			http(myYear, myMonth);

		};

		$scope.next = function() {
			if(myMonth == 12) {
				myYear++;
				myMonth = 1;
			} else {
				myMonth++;
			}
			http(myYear, myMonth);
		};

		$scope.selectTime = function() {
			(function($) {
				$.init();
				selectTime.addEventListener('tap', function() {
					var _self = this;
					if(_self.picker) {
						_self.picker.show(function(rs) {
							//							selectTime.innerText = rs.y.value + "年" + rs.m.value + "月";

							http(rs.y.value, rs.m.value);
						});
					} else {
						var optionsJson = this.getAttribute('data-options') || '{}';
						var options = JSON.parse(optionsJson);

						_self.picker = new $.DtPicker(options);
						_self.picker.show(function(rs) {;
							http(rs.m.value);

						});
					}
				});
			})(mui);

		};
	}
])