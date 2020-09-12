package com.trateg.bepresensimobile.util

import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.BaseResponseList
import retrofit2.Response

abstract class ErrorUtilsContract {
    abstract fun parseErrorList(response: Response<BaseResponseList>): BaseResponseList
    abstract fun parseError(response: Response<BaseResponse>): BaseResponse
}