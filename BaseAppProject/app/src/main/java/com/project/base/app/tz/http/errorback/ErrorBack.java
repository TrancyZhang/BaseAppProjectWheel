package com.project.base.app.tz.http.errorback;

import com.project.base.app.tz.bean.BaseData;

/**
 * Created by An4 on 2019-09-23.
 */

public class ErrorBack extends Throwable {

    BaseData baseData;

    public ErrorBack(BaseData baseData) {
        this.baseData = baseData;
    }
}
