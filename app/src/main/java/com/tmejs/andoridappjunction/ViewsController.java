package com.tmejs.andoridappjunction;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tmejs on 03.08.2017.
 */

public class ViewsController {

    /**
     * Zmienne związane z ładowaniem obrazka
     * To powinno byc w osobnej klasie definiujacej zmienne lub lepiej - pobrane z AWS jako param
     */

    private static ViewsController i;

    public static ViewsController getInstance() {
        if (i == null) {
            i = new ViewsController();
        }
        return i;

    }


    public interface OnClickListener<T> {
        void onClick(T object);
    }

    private ViewsController() {

    }

    /**
     * Pobranie tekstu z kontrolki o danym id
     *
     * @param id - Id kontrolki
     * @return Text z kontrolki lub null jesli brak tekstu/kontrolki
     */
    public String getText(Integer id) {
        View view = ApplicationController.getCurrentActivity().findViewById(id);
        //MRZAD 03.08.2017
        //Rzutowanie na TextView bo wszystkie kontrolki które oprogramowałem dziedziczył po TextView
        if (view == null) return null;
        else

        {
            if (!(view instanceof TextView)) return null;
            else {
                return ((TextView) view).getText().toString();
            }
        }

    }

    public <T> void setListInFlexBox(Integer id, List<T> list, List<Pair<String, String>> columnsList, final OnClickListener<T> myOnClick) {

        //Pobieramy tabele
        ViewGroup tl = ApplicationController.getCurrentActivity().findViewById(id);
        //Czyścimy tabele
        tl.removeAllViews();

        //Pobieramy listę fieldów
        if (list.isEmpty()) {
            return;
        } else {

            // Pobieramy liste fieldów
            Field[] fields = list.get(0).getClass().getFields();
            ArrayList<Field> finalList = new ArrayList<>();
            for (Pair<String, String> entry : columnsList) {
                for (Field field : fields) {
                    if (entry.first.equalsIgnoreCase(field.getName())) {
                        finalList.add(finalList.size(), field);
                        break;
                    }
                }
            }


            //Dla każdego elementu tworzymy nowy Layout
            for (final T obj : list) {
                //Utworzenie całego row
                LinearLayout mainLayout = new LinearLayout(ApplicationController.getCurrentActivity().getApplicationContext());
                mainLayout.setBackgroundColor(Color.LTGRAY);
                mainLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mainLayoutParams.setMargins(10, 10, 10, 10);
                mainLayout.setLayoutParams(mainLayoutParams);
                mainLayout.setPadding(10, 10, 10, 10);
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(5, Color.BLACK);
                drawable.setColor(Color.LTGRAY);

                mainLayout.setBackgroundDrawable(drawable);
                //Ustawiamy event jeśli był
                if (myOnClick != null) {
                    mainLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myOnClick.onClick(obj);
                        }
                    });
                }


                for (Pair<String, String> entry : columnsList) {
                    LinearLayout row = new LinearLayout(ApplicationController.getCurrentActivity().getApplicationContext());
                    row.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                    rowLayoutParams.setMargins(2, 2, 2, 2);
                    row.setLayoutParams(rowLayoutParams);

                    //TextView dla nazwy wartości
                    TextView tvName = new TextView(ApplicationController.getCurrentActivity().getApplicationContext());
                    LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tvName.setLayoutParams(margin);
//                    tvName.setTypeface(Typeface.DEFAULT_BOLD);
                    tvName.setTextSize(20.0f);
                    tvName.setTextColor(Color.BLACK);

                    tvName.setText(entry.second);

                    //TextView dla wartości
                    TextView tvValue = new TextView(ApplicationController.getCurrentActivity().getApplicationContext());
                    tvValue.setLayoutParams(margin);
                    tvValue.setTextSize(24.0f);
                    tvValue.setTextColor(Color.BLACK);
                    //Styl celki
                    tvValue.setTypeface(Typeface.DEFAULT_BOLD);
                    for (Field field : finalList) {
                        //Sprawdzamy czy user podał dany field do szukania

                        if (entry.first.equalsIgnoreCase(field.getName())) {

                            Object fieldValue = null;
                            try {

                                fieldValue = field.get(obj);
                            } catch (IllegalAccessException e) {

                            }
                            if (fieldValue != null)
                                tvValue.setText(fieldValue.toString());
                            else {
                                tvValue.setText("");
                            }
                            break;
                        }

                    }
                    row.addView(tvName);
                    row.addView(tvValue);
                    mainLayout.addView(row);
                }
                //Dodanie kafelki dla danego obiektu
                tl.addView(mainLayout);
            }
        }
    }


    public <T> void setListInTable(Integer id, List<T> list, List<Pair<String, String>> columnsList) {
        setListInTable(id, list, columnsList, Color.DKGRAY, Float.valueOf("24"), Color.WHITE, Color.WHITE, Float.valueOf("22"), Color.BLACK, null);
    }


    public <T> void setListInTable(Integer id, List<T> list, List<Pair<String, String>> columnsList, OnClickListener<T> myOnClick) {
        setListInTable(id, list, columnsList, Color.DKGRAY, Float.valueOf("24"), Color.WHITE, Color.WHITE, Float.valueOf("22"), Color.BLACK, myOnClick);
    }


    /**
     * Ustawienie listy w tabeli
     *
     * @param id
     * @param list
     * @param columnsList Mapa kolumn z mapowanego obiektu, wraz z tłumaczeniami.
     */
    public <T> void setListInTable(Integer id, final List<T> list, List<Pair<String, String>> columnsList, Integer headerBcgColor, Float headerSize, Integer headerColor, final Integer cellBgColor, Float cellTExtSize, Integer cellTextColor, final OnClickListener<T> myOnClick) {

        //Pobieramy tabele
        TableLayout tl = ApplicationController.getCurrentActivity().findViewById(id);
        //Czyścimy tabele
        tl.removeAllViews();
        tl.setStretchAllColumns(true);

//        TableRow mr = new TableRow(ApplicationController.getCurrentActivity().getApplicationContext());
//        mr.addView(new TextView(ApplicationController.getCurrentActivity().getApplicationContext()));
//        mr.setLayoutParams(new TableLayout.LayoutParams(0, 5, 1));
//        mr.setBackgroundColor(Color.BLACK);

        //Utworzenie headerów z kolumn
        TableRow tr = new TableRow(ApplicationController.getCurrentActivity().getApplicationContext());


        TableLayout.LayoutParams p1 = new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        p1.setMargins(2, 2, 2, 2);
        tr.setLayoutParams(p1);

        for (Pair<String, String> entry : columnsList) {
            TextView tv = new TextView(ApplicationController.getCurrentActivity().getApplicationContext());
            TableRow.LayoutParams margin = new TableRow.LayoutParams();
            margin.setMargins(2, 2, 2, 2);
            tv.setLayoutParams(margin);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setBackgroundColor(headerBcgColor);
            tv.setTextSize(headerSize);
            tv.setTextColor(headerColor);
            tv.setText(entry.second);
            tr.addView(tv);
        }
        tl.addView(tr);


        //Pobieramy listę fieldów
        if (list.isEmpty()) {
            return;
        } else {

            // Pobieramy liste fieldów
            Field[] fields = list.get(0).getClass().getFields();
            ArrayList<Field> finalList = new ArrayList<>();
            for (Pair<String, String> entry : columnsList) {
                for (Field field : fields) {
                    if (entry.first.equalsIgnoreCase(field.getName())) {
                        finalList.add(field);
                        break;
                    }
                }
            }
            //Iteracja po obiektach z listy
            for (final T obj : list) {
                final TableRow trd = new TableRow(ApplicationController.getCurrentActivity().getApplicationContext());
                if (myOnClick != null) {
                    trd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myOnClick.onClick(obj);
                        }
                    });

                }
                TableLayout.LayoutParams p2 = new TableLayout.LayoutParams(0, cellTExtSize.intValue() + cellTExtSize.intValue(), 1);
                p2.setMargins(2, 2, 2, 2);
                tr.setLayoutParams(p2);
                trd.setLayoutParams(p2);

                //Iteracja po fieldsacj
                for (Field field : finalList) {
                    //Sprawdzamy czy user podał dany field do szukania
                    for (Pair<String, String> entry : columnsList) {
                        if (entry.first.equalsIgnoreCase(field.getName())) {
                            //Tworzymy nowa celke
                            TextView tv = new TextView(ApplicationController.getCurrentActivity().getApplicationContext());
                            TableRow.LayoutParams margin = new TableRow.LayoutParams();
                            margin.setMargins(2, 2, 2, 2);
                            tv.setLayoutParams(margin);
                            //Styl celki
                            tv.setTypeface(Typeface.DEFAULT_BOLD);
                            tv.setBackgroundColor(cellBgColor);
                            tv.setTextSize(cellTExtSize);
                            tv.setTextColor(cellTextColor);
                            //Pobieramy wartość
                            try {
                                Object fieldValue = field.get(obj);
                                if (fieldValue != null)
                                    tv.setText(fieldValue.toString());
                                else {
                                    tv.setText("");
                                }
                                //Dodajemy textView do row
                                trd.addView(tv);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //Dodajemy row do tabeli
                tl.addView(trd);
                //Dodajemy margines
//                tl.addView(mr);
            }
        }
    }

    /**
     * Pobranie kolory kontrolki o danym id
     *
     * @param id - Id kontrolki
     * @return
     */
    @Nullable
    public Integer getColor(Integer id) {
        View view = ApplicationController.getCurrentActivity().findViewById(id);

        if (view != null) return view.getDrawingCacheBackgroundColor();
        else {
            return null;
        }
    }

    /**
     * Pobranie enabled kontrolki o danym id
     *
     * @param id
     * @return
     */
    @Nullable
    public Boolean getEnabled(Integer id) {
        View view = ApplicationController.getCurrentActivity().findViewById(id);
        if (view != null) return view.isEnabled();
        else {
            return null;
        }
    }


    /**
     * Ustawienie tekstu w kontrolce o danym id
     *
     * @param id - Id kontrolki
     * @return Text z kontrolki lub null jesli brak tekstu/kontrolki
     */
    public Boolean setText(Integer id, final String text) {
        final View view = ApplicationController.getCurrentActivity().findViewById(id);
        if (view != null) {
            if (!(view instanceof TextView)) return false;
            else {
                ApplicationController.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) view).setText(text);
                    }
                });

                return true;
            }
        } else {
            return false;
        }

    }

    public Boolean setBackground(Integer id, final Integer resId) {
        final View view = ApplicationController.getCurrentActivity().findViewById(id);

        if (view != null) {
            ApplicationController.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Drawable d = view.getResources().getDrawable(resId);
                    view.setBackground(d);
                }
            });

            return true;
        } else {
            return null;
        }
    }


    /**
     * Ustawienie koloru kontrolki o danym id
     *
     * @param id - Id kontrolki
     * @return
     */
    public Boolean setColor(Integer id, final Integer colorId) {
        final View view = ApplicationController.getCurrentActivity().findViewById(id);

        if (view != null) {
            ApplicationController.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.setBackgroundColor(colorId);
                }
            });

            return true;
        } else {
            return null;
        }
    }

    /**
     * Ustawienie Enabled w danej kontrolce
     *
     * @param id
     * @return
     */
    public Boolean setEnabled(Integer id, Boolean isEnabled) {
        View view = ApplicationController.getCurrentActivity().findViewById(id);

        if (view != null) {
            view.setEnabled(isEnabled);
            return true;
        } else {
            return false;
        }
    }

    public Boolean setVisible(Integer id, Boolean isVisible) {
        View view = ApplicationController.getCurrentActivity().findViewById(id);

        if (view != null) {
            if (isVisible)
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.INVISIBLE);

            return true;
        } else {
            return false;
        }

    }

    /**
     * Pobranie view dla danego id
     *
     * @param id
     * @return
     */
    private View getView(Integer id) {
        return ApplicationController.getCurrentActivity().findViewById(id);
    }


    public Boolean setProgressInProgressBar(Integer id, Integer value, Integer maxValue) {
        View view = ApplicationController.getCurrentActivity().findViewById(id);

        if (view != null) {
            if (view instanceof ProgressBar) {
                ((ProgressBar) view).setProgress(0);
                ((ProgressBar) view).setMax(maxValue);
                ((ProgressBar) view).setProgress(value);
                return true;
            }

        }
        return false;
    }
}
