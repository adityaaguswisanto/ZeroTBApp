<?php

use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\ConsultController;
use App\Http\Controllers\Api\DrugController;
use App\Http\Controllers\Api\PatientController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});

Route::namespace('Api')->group(function () {
    
    Route::get('report/patient', [PatientController::class, 'patient']);
    Route::get('report/drug', [PatientController::class, 'drug']);

    Route::prefix('auth')->group(function () {
        Route::post('register', [AuthController::class, 'register']);
        Route::post('login', [AuthController::class, 'login']);
        Route::post('forgot', [AuthController::class, 'forgot']);
    });
    Route::group(['prefix' => 'auth', 'middleware' => 'auth:sanctum'], function () {
        Route::get('logout', [AuthController::class, 'logout']);
        Route::get('user', [AuthController::class, 'user']);
        Route::post('profile', [AuthController::class, 'profile']);
        Route::put('user/{id}', [AuthController::class, 'update']);
        Route::put('medical/{id}', [AuthController::class, 'medical']);
    });
    Route::group(['prefix' => 'patient', 'middleware' => 'auth:sanctum'], function () {
        Route::get('/', [PatientController::class, 'index']);
    });
    Route::group(['prefix' => 'pill', 'middleware' => 'auth:sanctum'], function () {
        Route::get('/', [DrugController::class, 'index']);
    });
    Route::group(['prefix' => 'drug', 'middleware' => 'auth:sanctum'], function () {
        Route::post('', [DrugController::class, 'store']);
        Route::delete('/{id}', [DrugController::class, 'destroy']);
        Route::put('/{id}', [DrugController::class, 'update']);
    });
    Route::group(['prefix' => 'consult', 'middleware' => 'auth:sanctum'], function () {
        Route::post('', [ConsultController::class, 'store']);
        Route::get('/', [ConsultController::class, 'index']);
        Route::delete('/{id}', [ConsultController::class, 'destroy']);
    });
});