package com.xmx.androidkotlinbase.Tools.Data.Sync

import com.xmx.androidkotlinbase.Tools.Data.Cloud.ICloudEntity
import com.xmx.androidkotlinbase.Tools.Data.SQL.ISQLEntity

/**
 * Created by The_onE on 2016/5/29.
 */
interface ISyncEntity : ICloudEntity, ISQLEntity {
    var cloudId: String
}
