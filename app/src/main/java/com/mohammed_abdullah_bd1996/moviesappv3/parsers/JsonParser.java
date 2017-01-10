package com.mohammed_abdullah_bd1996.moviesappv3.parsers;
import com.mohammed_abdullah_bd1996.moviesappv3.models.ItemModel;
import com.mohammed_abdullah_bd1996.moviesappv3.models.ModelOfReview;
import com.mohammed_abdullah_bd1996.moviesappv3.urls.URLS;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
//Start of class JsonParser
public class JsonParser {
    public static List<ItemModel> parseJsonFeed(String feed){
        try {
            JSONObject jsonRootObject = new JSONObject(feed);//done
             JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("results");
            List<ItemModel> flowerList = new ArrayList<>();
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String poster_path = URLS.IMAGE_SOURCE + jsonObject.optString("poster_path");
                String backdrop_path = URLS.IMAGE_SOURCE + jsonObject.optString("backdrop_path");
                String overview = jsonObject.optString("overview");
                String release_date = jsonObject.optString("release_date");
                String id = jsonObject.optString("id");
                String vote_average = jsonObject.optString("vote_average");
                String original_title = jsonObject.optString("original_title");
                ItemModel itemModel = new ItemModel();
                itemModel.setPoster_path(poster_path);
                itemModel.setBackdrop_path(backdrop_path);
                itemModel.setOverview(overview);
                itemModel.setRelease_date(release_date);
                itemModel.setId(id);
                itemModel.setVote_average(vote_average);
                itemModel.setOriginal_title(original_title);
                flowerList.add(itemModel);
            }
            return flowerList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
//*************************************************************************************************
    public static String parseTrailer(String feed){
        try {
            JSONObject jsonRootObject = new JSONObject(feed);

            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("results");
            String code = "";
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String type = jsonObject.optString("type");
                if (type.equalsIgnoreCase("trailer"))
                    code = jsonObject.optString("key");
            }
            return code;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
//***********************************************************************************************************
    public static List<ModelOfReview> parseJsonReview(String feed){

        try {
            JSONObject jsonRootObject = new JSONObject(feed);//done
             JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("results");
            List<ModelOfReview> flowerList = new ArrayList<>();
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String author = jsonObject.optString("author");
                String content = jsonObject.optString("content");
                String url = jsonObject.optString("url");

                ModelOfReview modelOfReview = new ModelOfReview();
                modelOfReview.setAuthor(author);
                modelOfReview.setUrl(url);
                modelOfReview.setContent(content);
                flowerList.add(modelOfReview);
            }
            return flowerList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }


}//end of class
