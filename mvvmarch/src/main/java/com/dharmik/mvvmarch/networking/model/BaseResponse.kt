package com.dharmik.mvvmarch.networking.model

data class BaseResponse<T>(var returnCode: String, var returnMsg: String, var data: T) {

}