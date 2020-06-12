/*
* (C) 2015 Yandex LLC (https://yandex.com/)
*
* The source code of Java SDK for Yandex.Disk REST API
* is available to use under terms of Apache License,
* Version 2.0. See the file LICENSE for the details.
*/

package pro.butovanton.print.yandex.retrofit;


import java.io.IOException;

import pro.butovanton.print.yandex.exceptions.ServerIOException;
import pro.butovanton.print.yandex.json.ApiVersion;
import pro.butovanton.print.yandex.json.DiskInfo;
import pro.butovanton.print.yandex.json.Link;
import pro.butovanton.print.yandex.json.Operation;
import pro.butovanton.print.yandex.json.Resource;
import pro.butovanton.print.yandex.json.ResourceList;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface CloudApi {

    @GET("/")
    ApiVersion getApiVersion()
            throws IOException, ServerIOException;

    @GET("/v1/disk/operations/{operation_id}")
    Operation getOperation(@Path("operation_id") String operationId)
            throws IOException, ServerIOException;

    @GET("/v1/disk")
    DiskInfo getDiskInfo(@Query("fields") String fields)
            throws IOException, ServerIOException;

    @GET("/v1/disk/resources")
    Resource getResources(@Query("path") String path, @Query("fields") String fields,
                          @Query("limit") Integer limit, @Query("offset") Integer offset,
                          @Query("sort") String sort, @Query("preview_size") String previewSize,
                          @Query("preview_crop") Boolean previewCrop)
            throws IOException, ServerIOException;

    @GET("/v1/disk/resources/files")
    ResourceList getFlatResourceList(@Query("limit") Integer limit, @Query("media_type") String mediaType,
                                     @Query("offset") Integer offset, @Query("fields") String fields,
                                     @Query("preview_size") String previewSize,
                                     @Query("preview_crop") Boolean previewCrop)
            throws IOException, ServerIOException;

    @GET("/v1/disk/resources/last-uploaded")
    ResourceList getLastUploadedResources(@Query("limit") Integer limit, @Query("media_type") String mediaType,
                                          @Query("offset") Integer offset, @Query("fields") String fields,
                                          @Query("preview_size") String previewSize,
                                          @Query("preview_crop") Boolean previewCrop)
            throws IOException, ServerIOException;

  //  @PATCH("/v1/disk/resources/")
  //  Resource patchResource(@Query("path") String path, @Query("fields") String fields,
  //                         @Body TypedOutput body)
 //           throws IOException, ServerIOException;

    @GET("/v1/disk/resources/download")
    Link getDownloadLink(@Query("path") String path)
            throws IOException, ServerIOException;

    @POST("/v1/disk/resources/upload")
    Link saveFromUrl(@Query("url") String url, @Query("path") String path)
            throws IOException, ServerIOException;

    @GET("/v1/disk/resources/upload")
    Link getUploadLink(@Query("path") String path, @Query("overwrite") Boolean overwrite)
            throws IOException, ServerIOException;

    @POST("/v1/disk/resources/copy")
    Link copy(@Query("from") String from, @Query("path") String path,
              @Query("overwrite") Boolean overwrite)
            throws IOException, ServerIOException;

    @POST("/v1/disk/resources/move")
    Link move(@Query("from") String from, @Query("path") String path,
              @Query("overwrite") Boolean overwrite)
            throws IOException, ServerIOException;

    @PUT("/v1/disk/resources")
    Link makeFolder(@Query("path") String path)
            throws IOException, ServerIOException;

    @PUT("/v1/disk/resources/publish")
    Link publish(@Query("path") String path)
            throws IOException, ServerIOException;

    @PUT("/v1/disk/resources/unpublish")
    Link unpublish(@Query("path") String path)
            throws IOException, ServerIOException;

    @GET("/v1/disk/public/resources")
    Resource listPublicResources(@Query("public_key") String publicKey, @Query("path") String path,
                                 @Query("fields") String fields, @Query("limit") Integer limit,
                                 @Query("offset") Integer offset, @Query("sort") String sort,
                                 @Query("preview_size") String previewSize,
                                 @Query("preview_crop") Boolean previewCrop)
            throws IOException, ServerIOException;

    @GET("/v1/disk/public/resources/download")
    Link getPublicResourceDownloadLink(@Query("public_key") String publicKey,
                                       @Query("path") String path)
            throws IOException, ServerIOException;

    @POST("/v1/disk/public/resources/save-to-disk/")
    Link savePublicResource(@Query("public_key") String publicKey, @Query("path") String path,
                            @Query("name") String name)
            throws IOException, ServerIOException;

    @GET("/v1/disk/trash/resources")
    Resource getTrashResources(@Query("path") String path, @Query("fields") String fields,
                               @Query("limit") Integer limit, @Query("offset") Integer offset,
                               @Query("sort") String sort, @Query("preview_size") String previewSize,
                               @Query("preview_crop") Boolean previewCrop)
            throws IOException, ServerIOException;
}
