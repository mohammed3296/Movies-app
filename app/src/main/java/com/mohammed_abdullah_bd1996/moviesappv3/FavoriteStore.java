package com.mohammed_abdullah_bd1996.moviesappv3;
import android.content.Context;
import android.content.SharedPreferences;
import com.mohammed_abdullah_bd1996.moviesappv3.models.ModelOfFavoriteMovie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
public class FavoriteStore {
    private static final String PREFKEY = "favorites";
    private SharedPreferences favoritePrefs;
    public FavoriteStore(Context context) {
        favoritePrefs = context.getSharedPreferences(PREFKEY, Context.MODE_PRIVATE);
    }
    public List<ModelOfFavoriteMovie> findAll() {

        Map<String, ?> notesMap = favoritePrefs.getAll();

        SortedSet<String> keys = new TreeSet<String>(notesMap.keySet());

        List<ModelOfFavoriteMovie> noteList = new ArrayList<>();
        for (String key : keys) {
            ModelOfFavoriteMovie note = new ModelOfFavoriteMovie();
            note.setTitleKey(key);
            note.setIdValue((String) notesMap.get(key));
            noteList.add(note);
        }

        return noteList;
    }

    public boolean update(ModelOfFavoriteMovie note) {

        SharedPreferences.Editor editor = favoritePrefs.edit();
        editor.putString(note.getTitleKey(), note.getIdValue());
        editor.commit();
        return true;
    }

}
