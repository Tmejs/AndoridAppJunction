package com.tmejs.andoridappjunction;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tmejs on 24.07.2017.
 */

public class AppParams {

    public static final String JOIN_SERVICE_NAME = "/join";
    public static final String INIITIAL_PLAYER_AMOUNT = "INIITIAL_PLAYER_AMOUNT";
    public static final String PLAYER_NAME = "PLAYER_NAME";
    private static AppParams instance;


    //Nazwa Shared preferences dla danego APP_PARAMS;
    public final static String SHARED_PREFERENCES_NAME = "APP_PARAMS";
    private final static String SHARED_PREFERENCES_INITIALIZATION_MARK = "INITIALIZED";

    private final HashMap<String, String> values;

    public final SharedPreferences sharedPreferences;

    /**
     * Parametry aplikacji
     * <p>
     * Wszystkie nazwy parametrów z wielkiej litery.
     * Każdy parametr powinien mieć modyfikator "public "
     * Każdy parametr typu Object
     * "public Object MIEJSCE;"
     */
    public final static String HTTP_PROTOCOL_PREFIX="http://";
    public final static String WEB_SERWER_ADDRESS="10.100.48.165:8080/game/";
    public final static String WEB_ASK_GAME_SERVLET_ADDRESS="ask";
    public final static String INCOMING_SERVLET_PATH="";
    public final static String GET_NEW_GAME_SERVLET_PATH="/selectMinigame";
    public final static String HTTP_PARAM_DELIMETER="&";


    //Names for params
    public final static String COMPETITION_ID="COMPETITION_ID";
    public static final String PLAYER_ID = "PLAYER_ID";



    public static AppParams getInstance(SharedPreferences sharedPreferences) {
        if (instance == null) {
            instance = new AppParams(sharedPreferences);
        }

        return instance;
    }

    private AppParams(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        values = new HashMap<>();

        //Sprawdzamy czy zainicjowano już parametry
        if (checkIsParamsSet()) {
            fillDataBySharedPreferences();
        }

    }


    /**
     * Ustawienie wartości w obiekcie na podstawie pobranych danych z SharedPreferences
     */
    public void fillDataBySharedPreferences() {
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            setParamInApp(entry.getKey(), entry.getValue());
        }
    }


    private void setParamInApp(String name, Object value) {
        //Ustawienie w pamięci programu
        if (value != null)
            this.values.put(name, value.toString());
    }

    /**
     * Ustawienie wartości parametru w statycznym polu klasy
     *
     * @param name
     * @param value
     * @return
     */
    public void setParamValue(String name, Object value) {

        //Ustawienie w pamięci programu
        if (value != null) {
            this.values.put(name, value.toString());
            //Ustawiamy wartosc w sharedPreferences
            setValueInSharedPreferences(name, value.toString());
        } else {
            this.values.put(name, null);
            setValueInSharedPreferences(name, null);
        }


        //Ustawiliśmy jakąś wartość więc w pliku ustawiamy że już po inicjalizacji
        setSharedPreferencesInitialized();

    }

    /**
     * Pobranie wartości parametru
     *
     * @param paramName
     * @return
     */
    public String getParamValue(String paramName) {
        return values.get(paramName);
    }


    /**
     * Ustawienie znacznika że zainicjowano plik z konfiguracja.
     */
    private void setSharedPreferencesInitialized() {
        setValueInSharedPreferences(SHARED_PREFERENCES_INITIALIZATION_MARK, Boolean.toString(true));
    }

    /**
     * Ustawienie wartości w pliku SharedPreferences
     *
     * @param name  Klucz
     * @param value wartość
     */
    private void setValueInSharedPreferences(String name, String value) {
        //Pobieramy edytor
        SharedPreferences.Editor ed = sharedPreferences.edit();
        //Ustawaimy wartość
        ed.putString(name, value);

        //Commit
        ed.commit();
    }

    /**
     * Ustawienie wartości parametru w obiekcie PARAMS
     *
     * @param entries
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void setParamsValues(Map<String, Object> entries) throws NoSuchFieldException, IllegalAccessException {

        for (Map.Entry<String, Object> entry : entries.entrySet()) {
            setParamValue(entry.getKey(), entry.getValue());
        }
    }


    /**
     * Sprawdzenie czy zainicjowano już plik z parametrami
     *
     * @return
     */
    public Boolean checkIsParamsSet() {


        //Sprawdzenie czy ustawiony znacznik inicjalizacji
        return sharedPreferences.contains(SHARED_PREFERENCES_INITIALIZATION_MARK);
    }


    /**
     * Wyczyszczenie wszystkich parametrów
     */
    public void clearParams() {
        sharedPreferences.edit().clear().commit();

    }

}
