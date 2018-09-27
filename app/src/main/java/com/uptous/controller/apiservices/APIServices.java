package com.uptous.controller.apiservices;


import com.google.gson.JsonElement;
import com.uptous.model.AlbumDetailResponseModel;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.CommunityTitleModel;
import com.uptous.model.ContactListResponseModel;
import com.uptous.model.EventResponseModel;
import com.uptous.model.FeedResponseModel;
import com.uptous.model.FileResponseModel;
import com.uptous.model.GetAllAnnouncementResponseModel;
import com.uptous.model.GetAllCommentResponseModel;
import com.uptous.model.InvitationResponseModel;
import com.uptous.model.PhotoAlbumResponseModel;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.model.ProfileResponseModel;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.model.SignUpResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/*
 * FileName : APIServices.java
 * Dependencies :
 * Description : Get and post methods.
 * Classes : APIServices.java
 */
public interface APIServices {

    @GET("api/profile")
    Call<PostCommentResponseModel> login();

//    @GET("api/members")     // /api/members/community/0/search/0/limit/150/offset/0
//    Call<List<ContactListResponseModel>> GetContactList();

    @GET("api/members/community/0/search/0/limit/{limit}/offset/{offset}")
    Call<List<ContactListResponseModel>> GetContactList(@Path("limit") int limit,
                                                        @Path("offset") int offset);

    @GET("api/members/community/0/search/{search}/limit/{limit}/offset/{offset}")
    Call<List<ContactListResponseModel>> GetContactListForSearch(@Path("search") String search,
                                                                 @Path("limit") int limit,
                                                                 @Path("offset") int offset);


    @GET("api/communities")
    Call<List<CommnunitiesResponseModel>> GetCommunity();

    @GET("api/feed")
    Call<List<FeedResponseModel>> GetNewsFeed();

    @GET("api/signupsheets/thin/days/365")
    Call<List<SignUpResponseModel>> GetSignUp();

    @GET("api/photoalbums")
    Call<List<PhotoAlbumResponseModel>> GetAllAlbumPhoto();

    @GET("api/attachment/days/3650")
    Call<List<FileResponseModel>> GetAttachment();

    @GET("/api/photothumbs/album/{albumId}")
    Call<List<AlbumDetailResponseModel>> GetAlbum(@Path("albumId") int albumId);

    @GET("api/signupsheets/opportunity/{opportunityId}")
    Call<List<SignUpDetailResponseModel>> GetSignUpDetail(@Path("opportunityId") int id);

    @GET("api/signupsheets/opportunity/{opportunityId}/item/{item_id}/Add")
    Call<List<SignUpDetailResponseModel>> GetItem(@Path("opportunityId") int id, @Path("item_id") int item_id);

    @GET("api/events/365")
    Call<List<EventResponseModel>> GetEvent();

    @GET("api/invites")
    Call<List<InvitationResponseModel>> GetInvitation();

    @GET("api/comments/feed/{feedId}")
    Call<List<GetAllCommentResponseModel>> GetAllComment(@Path("feedId") int id);

    @GET("api/posts/announcement/{newsItemId}")
    Call<List<GetAllAnnouncementResponseModel>> GetAllAnnouncementComment(@Path("newsItemId") int id);

    @GET("api/profile")
    Call<ProfileResponseModel> ProfileDetail();

    @GET("api/photoalbums/community/{communityId}")
    Call<List<CommunityTitleModel>> GetTitleInCommunity(@Path("communityId") int id);


    @FormUrlEncoded
    @POST("api/profile/update")
    Call<ProfileResponseModel> UpdateProfile(@Field("firstname") String firstname, @Field("lastname") String lastname,
                                             @Field("email") String email, @Field("phone") String phone,
                                             @Field("photo") String photo);

    @FormUrlEncoded
    @POST("api/comments/feed/{feedId}")
    Call<PostCommentResponseModel> PostComment(@Path("feedId") int id, @Field("contents") String contents);

    @FormUrlEncoded
    @POST("api/invites/{invitationId}/accept")
    Call<JsonElement> PostInvite(@Path("invitationId") int invitationId, @Field("contents") String contents);


    @FormUrlEncoded
    @POST("api/messages/community/{communityId}")
    Call<PostCommentResponseModel> PostMessage(@Path("communityId") int communityId, @Field("subject") String subject,
                                               @Field("contents") String contents);

    @FormUrlEncoded
    @POST("api/posts/announcement/{newsItemId}")
    Call<PostCommentResponseModel> PostReplyAll(@Path("newsItemId") int newsItemId, @Field("contents") String contents);

    //https://www.uptous.com/api/photoalbums/community/351/title/myNewAlbum
    @FormUrlEncoded
    @POST("api/photoalbums/community/{communityId}/title/{title}")
    Call<PostCommentResponseModel> PostNewPicture(@Path("title") String title, @Path("communityId") int communityId,
                                                  @Field("caption") String caption, @Field("filename") String filename,
                                                  @Field("photo") String photo);

    //sample url https://www.uptous.com/api/photoalbums/community/351/album/26605
    @FormUrlEncoded
    @POST("api/photoalbums/community/{communityId}/album/{album}")
    Call<PostCommentResponseModel> PostPictureinAlbum(@Path("album") int album, @Path("communityId") int communityId,
                                                      @Field("caption") String caption, @Field("filename") String filename,
                                                      @Field("photo") String photo);

    @FormUrlEncoded
    @POST("api/signupsheets/opportunity/{opportunityId}/item/{item_id}/Add")
    Call<PostCommentResponseModel> SignUp_Send(@Path("opportunityId") int opportunityId, @Path("item_id") int item_id,
                                               @Field("comment") String comment, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/signupsheets/opportunity/{opportunityId}/item/{item_id}/Add")
    Call<PostCommentResponseModel> SignUp_Send_RSPV(@Path("opportunityId") int opportunityId, @Path("item_id") int item_id,
                                                    @Field("comment") String comment, @Field("numberOfAttendees") String numberOfAttendees);

    @FormUrlEncoded
    @POST("api/signupsheets/opportunity/{opportunityId}/item/{item_id}/Del")
    Call<PostCommentResponseModel> Cancel_Assignment(@Path("opportunityId") int opportunityId,
                                                     @Path("item_id") int item_id, @Field("comment") String comment);

}
