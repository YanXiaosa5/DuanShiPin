package com.jumang.shortvideo.api.OkHttpUtils.builder;

import com.jumang.shortvideo.api.OkHttpUtils.OkHttpUtils;
import com.jumang.shortvideo.api.OkHttpUtils.request.OtherRequest;
import com.jumang.shortvideo.api.OkHttpUtils.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
