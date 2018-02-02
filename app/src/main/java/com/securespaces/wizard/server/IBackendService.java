package com.securespaces.wizard.server;

/**
 * Created by raymond on 12/30/16.
 */

import com.google.gson.JsonObject;

import retrofit.http.Body;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface IBackendService {

    @PUT("/api/v3/device/spaces/{spaceId}/userData")
    JsonObject putUserInformation(@Path("spaceId") String spaceId, @Body UserInfo data);

}
