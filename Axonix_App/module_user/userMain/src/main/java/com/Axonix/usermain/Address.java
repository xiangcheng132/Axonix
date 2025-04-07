package com.Axonix.usermain;

import java.util.List;

public class Address {
    public static class Province {
        private String name;
        private List<City> cities;

        public String getName() { return name; }
        public List<City> getCities() { return cities; }
    }

    public static class City {
        private String name;
        private List<District> districts;

        public String getName() { return name; }
        public List<District> getDistricts() { return districts; }
    }

    public static class District {
        private String name;
        public String getName() { return name; }
    }
}