package com.trateg.bepresensimobile.util

import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.BaseResponseList
import org.json.JSONObject
import retrofit2.Response

object ErrorUtils : ErrorUtilsContract() {
    override fun parseErrorList(response: Response<BaseResponseList>): BaseResponseList {
        val bodyObj: JSONObject?
        var success: Boolean
        var message: String?

        try {
            val errorBody: String? = response.errorBody()!!.string()

            if (errorBody != null) {
                bodyObj = JSONObject(errorBody)

                success = bodyObj.getBoolean("success")
                message = bodyObj.getString("message")
            } else {
                success = false;
                message = "Unable to parse error"
            }
        } catch (e: Exception) {
            e.printStackTrace();

            success = false;
            message = "Unable to parse error"
        }

        return BaseResponseList(data=null,message=message,success=success)
    }

    override fun parseError(response: Response<BaseResponse>): BaseResponse {
        val bodyObj: JSONObject?
        var success: Boolean
        var message: String?

        try {
            val errorBody: String? = response.errorBody()!!.string()

            if (errorBody != null) {
                bodyObj = JSONObject(errorBody)

                success = bodyObj.getBoolean("success")
                message = bodyObj.getString("message")
            } else {
                success = false;
                message = "Unable to parse error"
            }
        } catch (e: Exception) {
            e.printStackTrace();

            success = false;
            message = "Unable to parse error"
        }

        return BaseResponse(data=null,message=message,success=success)
    }
}