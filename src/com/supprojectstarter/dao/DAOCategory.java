package com.supprojectstarter.dao;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supprojectstarter.beans.Category;
import com.supprojectstarter.beans.Project;
import com.supprojectstarter.utilities.JSONParser;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DAOCategory extends DAO<Category> {
    private static final String categoriesURI = "http://10.0.2.2:8080/SPS/rest/categories/";
    private static DAOCategory instance = new DAOCategory();
    ObjectMapper mapper = new ObjectMapper();
    private JSONParser jsonParser = new JSONParser();

    private DAOCategory() {
    }

    public static DAOCategory getInstance() {
        return instance;
    }

    @Override
    public void insert(Category obj) {
    }

    @Override
    public void update(Category obj) {
    }

    @Override
    public Category find(int id) {
        return null;
    }

    @Override
    public Category findByName(String name) {
        String json = "";
        json = jsonParser.makeGetRequest(categoriesURI + name);
        Category category = null;
        try {
            if (json != null) {
                category = mapper.readValue(json, Category.class);
                DateTime now = new DateTime();
                List<Project> projects = category.getProjects();
                Iterator<Project> iterator = projects.iterator();
                while (iterator.hasNext()) {
                    Project p = iterator.next();
                    if (p.StartDateTime().isAfter(now) || p.EndDate().isBefore(now)) {
                        iterator.remove();
                    }
                }
                category.setProjects(projects);
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
        return category;
    }

    @Override
    public List<Category> findAll() {
        String json = "";
        json = jsonParser.makeGetRequest(categoriesURI);
        List<Category> categories = null;
        try {
            if (json != null) {
                categories = mapper.readValue(json, new TypeReference<List<Category>>() {
                });
                DateTime now = new DateTime();
                for (Category c : categories) {
                    List<Project> projects = c.getProjects();
                    Iterator<Project> iterator = projects.iterator();
                    while (iterator.hasNext()) {
                        Project p = iterator.next();
                        if (p.StartDateTime().isAfter(now) || p.EndDate().isBefore(now)) {
                            iterator.remove();
                        }
                    }
                    c.setProjects(projects);
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
        return categories;
    }

    public List<String> findAllNames() {
        List<Category> categoriesObjects = DAOCategory.getInstance().findAll();
        List<String> categoriesNames = new ArrayList<String>();
        for (Category c : categoriesObjects) {
            categoriesNames.add(c.getName());
        }
        return categoriesNames;
    }
}