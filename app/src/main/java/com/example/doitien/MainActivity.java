package com.example.doitien;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    private ImageView Image1;
    private ImageView Image2;
    private Spinner spinner1;
    private Spinner spinner2;
    private EditText number1;
    private EditText number2;
    private Button bt;
    private TextView coin1;
    private TextView coin2;
    private ArrayList<String> list = new ArrayList<>();
    static String s1 = "";
    static String s2 = "";
    static String fullfrom = "";
    static String fullto = "";
    static String secondfrom = "";
    static String secondto = "";
    private static final String url = "http://api.geonames.org/countryInfoJSON?username=quangtruong";
    public static List<Country> ListCountry;
    private int OK = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            ListCountry = new LoadData().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();

        for (int i = 0; i < ListCountry.size(); i++) {
            list.add(ListCountry.get(i).getCountryName());
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String url = "https://img.geonames.org/flags/x/" + ListCountry.get(i).getCountryCode().toLowerCase() + ".gif";
                Picasso.get().load(url).into(Image1);
                coin1.setText(ListCountry.get(i).getCurrencyCode());
                s1 = ListCountry.get(i).getCurrencyCode().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String url = "https://img.geonames.org/flags/x/" + ListCountry.get(i).getCountryCode().toLowerCase() + ".gif";
                Picasso.get().load(url).into(Image2);
                coin2.setText(ListCountry.get(i).getCurrencyCode());
                s2 = ListCountry.get(i).getCurrencyCode().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnButtonPressed();
            }
        });
    }


    private class LoadData extends AsyncTask<Void, Void, List<Country>> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<Country> doInBackground(Void... voids) {
            List<Country> countries = new ArrayList<>();
            StringBuilder data = new StringBuilder();
            try {
                URL urlConnection = new URL(url);
                InputStreamReader in = new InputStreamReader(urlConnection.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(in);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    data.append(line);
                }
                countries = parseJsonString(data.toString());


            } catch (MalformedURLException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return countries;
        }

        @Override
        protected void onPostExecute(List<Country> countries) {
//            ListCountry  = countries;
            OK = 1;
        }
    }
    private List<Country> parseJsonString(String data) {
        List<Country> result = new ArrayList();

        try {
            JSONObject responseObject = new JSONObject(data);
            JSONArray earthquakes = responseObject.getJSONArray("geonames");
            int length = earthquakes.length();
            for (int idx = 0; idx < length; idx++) {

                JSONObject c = (JSONObject) earthquakes.get(idx);
                String code = c.getString("countryCode");
                result.add(
                        new Country(
                                c.getString("countryCode"),
                                c.getString("countryName"),
                                c.getString("currencyCode")));

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private class ConnectionInternet extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://" + s1 + ".fxexchangerate.com/" + s2 + ".xml";
            StringBuilder data = new StringBuilder();
            try {
                URL urlConnection = new URL(url);
                InputStreamReader in = new InputStreamReader(urlConnection.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(in);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    data.append(line);
                }
                parseXML(data.toString());

            } catch (MalformedURLException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            exchange();
            super.onPostExecute(unused);
        }
    }

    public void OnButtonPressed() {
        if(s1.equals(s2))
        {
            Toast.makeText(this, "Xin thay đổi loại tiền bạn muốn chuyển sang", Toast.LENGTH_SHORT).show();
        } else if(number1.getText().toString().equals("")) {
            Toast.makeText(this, "Xin nhập số tiền bạn muốn chuyển đổi", Toast.LENGTH_SHORT).show();
        }
        else
        {
            new ConnectionInternet().execute();
        }

    }
    private void parseXML(String data) {

        XMLDOMParser parser = new XMLDOMParser();
        Document document = parser.getDocument(data);
        NodeList nodeList = document.getElementsByTagName("item");
        String tygia = "";
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            NodeList DescriptionNode = element.getElementsByTagName("description");
            Element DescriptionEle = (Element) DescriptionNode.item(i);
            tygia = Html.fromHtml(DescriptionEle.getFirstChild().getNodeValue().trim()).toString();
            tygia = tygia.substring(0, tygia.lastIndexOf(System.lineSeparator()));
            tygia = tygia.substring(0, tygia.lastIndexOf(System.lineSeparator()));
            StringTokenizer token = new StringTokenizer(tygia, "=", false);
            fullfrom = token.nextToken().trim();
            fullto = token.nextToken().trim();
            token = new StringTokenizer(fullfrom, " ", false);
            secondfrom = token.nextToken().trim();
            token = new StringTokenizer(fullto, " ", false);
            secondto = token.nextToken().trim();
        }
    }
    private void exchange()
    {
        float n1,n2,u1,f;
        n1=Float.parseFloat(secondfrom);
        n2=Float.parseFloat(secondto);
        u1=Float.parseFloat(number1.getText().toString());

        f=(u1*n2)/n1;

        String temp= Float.toString(f);
        number2.setText(temp);
    }
    private void init() {
        Image1 = findViewById(R.id.first);
        Image2 = findViewById(R.id.second);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        bt = findViewById(R.id.ex);
        coin1 = findViewById(R.id.tvCoin1);
        coin2 = findViewById(R.id.tvCoin2);
    }
}