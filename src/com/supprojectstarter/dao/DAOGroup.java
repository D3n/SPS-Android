package com.supprojectstarter.dao;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supprojectstarter.beans.Group;
import com.supprojectstarter.utilities.JSONParser;

import java.io.IOException;
import java.util.List;

public class DAOGroup extends DAO<Group> {
    private static final String groupsURI = "http://10.0.2.2:8080/SPS/rest/groups/";
    private static DAOGroup instance = new DAOGroup();
    ObjectMapper mapper = new ObjectMapper();
    private JSONParser jsonParser = new JSONParser();

    private DAOGroup() {
    }

    public static DAOGroup getInstance() {
        return instance;
    }

    @Override
    public void insert(Group obj) {
        jsonParser.makePostRequest(groupsURI, obj);
    }

    @Override
    public void update(Group obj) {
    }

    @Override
    public Group find(int id) {
        return null;
    }

    @Override
    public Group findByName(String name) {
        String json = "";
        json = jsonParser.makeGetRequest(groupsURI + name);
        Group group = null;
        try {
            if (json != null) {
                group = mapper.readValue(json, Group.class);
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
        return group;
    }

    @Override
    public List<Group> findAll() {
        return null;
    }
}