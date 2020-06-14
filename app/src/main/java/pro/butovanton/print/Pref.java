package pro.butovanton.print;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Pref {

private SharedPreferences msharedPreferences;

public Pref(Context context) {
    msharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
}

public void saveTel(String link) {
    SharedPreferences.Editor editor = msharedPreferences.edit();
    editor.putString("tel",link);
    editor.commit();
}

public String getTel() {
   return msharedPreferences.getString("tel","");
}

public void saveToken(String token) {
    SharedPreferences.Editor editor = msharedPreferences.edit();
    editor.putString("token", token);
    editor.commit();
}

    public String getToken() {
        return msharedPreferences.getString("token","");
    }
}
