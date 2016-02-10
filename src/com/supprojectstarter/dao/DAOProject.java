package com.supprojectstarter.dao;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supprojectstarter.beans.Project;
import com.supprojectstarter.utilities.JSONParser;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class DAOProject extends DAO<Project> {
    private static final String projectsURI = "http://10.0.2.2:8080/SPS/rest/projects/";
    private static DAOProject instance = new DAOProject();
    ObjectMapper mapper = new ObjectMapper();
    private JSONParser jsonParser = new JSONParser();

    private DAOProject() {
    }

    public static DAOProject getInstance() {
        return instance;
    }

    @Override
    public void insert(Project obj) {
        jsonParser.makePostRequest(projectsURI, obj);
    }

    @Override
    public void update(Project obj) {
    }

    @Override
    public Project find(int id) {
        String json = "";
        json = jsonParser.makeGetRequest(projectsURI + id);
        Project project = null;
        try {
            if (json != null) {
                project = mapper.readValue(json, Project.class);
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
        return project;
    }

    @Override
    public Project findByName(String name) {
        String json = "";
        json = jsonParser.makeGetRequest(projectsURI + "byName/" + name);
        Project project = null;
        try {
            if (json != null) {
                project = mapper.readValue(json, Project.class);
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
        return project;
    }

    @Override
    public List<Project> findAll() {
        String json = "";
        json = jsonParser.makeGetRequest(projectsURI);
        List<Project> projects = null;
        try {
            if (json != null) {
                projects = mapper.readValue(json, new TypeReference<List<Project>>() {
                });
                DateTime now = new DateTime();
                Iterator<Project> iterator = projects.iterator();
                while (iterator.hasNext()) {
                    Project p = iterator.next();
                    if (p.StartDateTime().isAfter(now) || p.EndDate().isBefore(now)) {
                        iterator.remove();
                    }
                }
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
        return projects;
    }
}