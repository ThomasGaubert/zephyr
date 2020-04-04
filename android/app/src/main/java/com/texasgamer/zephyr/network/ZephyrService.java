package com.texasgamer.zephyr.network;

import com.texasgamer.zephyr.model.api.ZephyrApiVersion;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Service definition for API provided by Zephyr desktop client running on local network.
 */
public interface ZephyrService {
    @GET("api/version")
    Call<ZephyrApiVersion> getVersion();
}
