package com.supprojectstarter.dao;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supprojectstarter.beans.User;
import com.supprojectstarter.utilities.JSONParser;

import java.io.IOException;
import java.util.List;

public class DAOUser extends DAO<User> {
    private static final String usersURI = "http://10.0.2.2:8080/SPS/rest/users/";
    private static DAOUser instance = new DAOUser();
    ObjectMapper mapper = new ObjectMapper();
    private JSONParser jsonParser = new JSONParser();

    private DAOUser() {
    }

    public static DAOUser getInstance() {
        return instance;
    }

    @Override
    public void insert(User obj) {
        jsonParser.makePostRequest(usersURI, obj);
    }

    @Override
    public void update(User obj) {
        jsonParser.makePutRequest(usersURI, obj);
    }

    @Override
    public User find(int id) {
        return null;
    }

    @Override
    public User findByName(String email) {
        String json = "";
        json = jsonParser.makeGetRequest(usersURI + email);
        User user = null;
        try {
            if (json != null) {
                user = mapper.readValue(json, User.class);
            } else {
                return null;
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        String json = "";
        json = jsonParser.makeGetRequest(usersURI);
        List<User> users = null;
        try {
            if (json != null) {
                users = mapper.readValue(json, new TypeReference<List<User>>() {
                });
            } else {
                return null;
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}