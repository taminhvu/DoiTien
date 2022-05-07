package com.example.doitien;

import java.io.Serializable;

public class Country implements Serializable {

    public String CountryCode;
    public String CountryName;
    public String CurrencyCode;

    public Country ()
    {
    }

    public Country(String countryCode,String countryName, String currentcyCode) {

        CountryCode = countryCode;
        CountryName = countryName;
        CurrencyCode = currentcyCode;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        this.CountryCode = countryCode;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        this.CountryName = countryName;
    }
}
