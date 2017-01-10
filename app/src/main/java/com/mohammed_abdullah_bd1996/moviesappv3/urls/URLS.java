package com.mohammed_abdullah_bd1996.moviesappv3.urls;
/**this
 * class for all
 * urls that are used
 * in my app
 * for facilitation
 * understand my code
 * all variables is static to access them
 * by class name
 * by mohammed abdu
 */

public class URLS {
    //the main url
    public static final String MAIN_URL_FROM_DESCRIPTION = "http://api.themoviedb.org/3/movie/",
    //To fetch trailers i  want to make a request to the /movie/{id}/videos endpoint.
    VEDIO_TERIAL = "/videos?api_key=0d3a607759ddd6f89e93d6228e834b3f",
    //To fetch reviews i want to make a request to the /movie/{id}/reviews endpoint
            VEDIO_REVIEW = "/reviews?api_key=0d3a607759ddd6f89e93d6228e834b3f",
    //The sort order can be by most popular, or by top rated
            POPULAR_MOVIES = "http://api.themoviedb.org/3/movie/popular?api_key=0d3a607759ddd6f89e93d6228e834b3f",
            TOP_RATED_MOVIES = "http://api.themoviedb.org/3/movie/top_rated?api_key=0d3a607759ddd6f89e93d6228e834b3f",
    //for Picasso
            IMAGE_SOURCE = "http://image.tmdb.org/t/p/w185/";
}
