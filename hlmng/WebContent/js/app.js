/**
 * Created by Sandeep on 01/06/14.
 */

angular.module('userApp',['ui.router','ngResource','userApp.controllers','userApp.services']);

angular.m/**
 * Created by Sandeep on 01/06/14.
 */

angular.module('userApp',['ui.router','ngResource','userApp.controllers','userApp.services']);

angular.module('userApp').config(function($stateProvider,$httpProvider){
    $stateProvider.state('users',{
        url:'/users',
        templateUrl:'partials/users.html',
        controller:'UserListController'
    }).state('viewUser',{
       url:'/users/:id/view',
       templateUrl:'partials/user-view.html',
       controller:'UserViewController'
    }).state('newUser',{
        url:'/users/new',
        templateUrl:'partials/user-add.html',
        controller:'UserCreateController'
    }).state('editUser',{
        url:'/users/:id/edit',
        templateUrl:'partials/user-edit.html',
        controller:'UserEditController'
    });
}).run(function($state){
   $state.go('users');/**
 * Created by Sandeep on 01/06/14.
 */

angular.module('userApp',['ui.router','ngResource','userApp.controllers','userApp.services']);

angular.module('userApp').config(function($stateProvider,$httpProvider){
    $stateProvider.state('users',{
        url:'/users',
        templateUrl:'partials/users.html',
        controller:'UserListController'
    }).state('viewUser',{
       url:'/users/:id/view',
       templateUrl:'partials/user-view.html',
       controller:'UserViewController'
    }).state('newUser',{
        url:'/users/new',
        templateUrl:'partials/user-add.html',
        controller:'UserCreateController'
    }).state('editUser',{
        url:'/users/:id/edit',
        templateUrl:'partials/user-edit.html',
        controller:'UserEditController'
    });
}).run(function($state){
   $state.go('users');/**
 * Created by Sandeep on 01/06/14.
 */

angular.module('userApp',['ui.router','ngResource','userApp.controllers','userApp.services']);

angular.module('userApp').config(function($stateProvider,$httpProvider){
    $stateProvider.state('users',{
        url:'/users',
        templateUrl:'partials/users.html',
        controller:'UserListController'
    }).state('viewUser',{
       url:'/users/:id/view',
       templateUrl:'partials/user-view.html',
       controller:'UserViewController'
    }).state('newUser',{
        url:'/users/new',
        templateUrl:'partials/user-add.html',
        controller:'UserCreateController'
    }).state('editUser',{
        url:'/users/:id/edit',
        templateUrl:'partials/user-edit.html',
        controller:'UserEditController'
    });
}).run(function($state){
   $state.go('users');odule('userApp').config(function($stateProvider,$httpProvider){
    $stateProvider.state('users',{
        url:'/users',
        templateUrl:'partials/users.html',
        controller:'UserListController'
    }).state('viewUser',{
       url:'/users/:id/view',
       templateUrl:'partials/user-view.html',
       controller:'UserViewController'
    }).state('newUser',{
        url:'/users/new',
        templateUrl:'partials/user-add.html',
        controller:'UserCreateController'
    }).state('editUser',{
        url:'/users/:id/edit',
        templateUrl:'partials/user-edit.html',
        controller:'UserEditController'
    });
}).run(function($state){
   $state.go('users');
});