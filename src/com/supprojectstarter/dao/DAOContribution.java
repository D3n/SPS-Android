package com.supprojectstarter.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supprojectstarter.beans.Contribution;
import com.supprojectstarter.utilities.JSONParser;

import java.util.List;

public class DAOContribution extends DAO<Contribution> {
    private static final String contributionsURI = "http://10.0.2.2:8080/SPS/rest/contributions/";
    private static DAOContribution instance = new DAOContribution();
    ObjectMapper mapper = new ObjectMapper();
    private JSONParser jsonParser = new JSONParser();

    private DAOContribution() {
    }

    public static DAOContribution getInstance() {
        return instance;
    }

    @Override
    public void insert(Contribution obj) {
        jsonParser.makePostRequest(contributionsURI, obj);
    }

    @Override
    public void update(Contribution obj) {
    }

    @Override
    public Contribution find(int id) {
        return null;
    }

    @Override
    public Contribution findByName(String name) {
        return null;
    }

    @Override
    public List<Contribution> findAll() {
        return null;
    }
}