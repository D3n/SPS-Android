package com.supprojectstarter.utilities;

import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supprojectstarter.beans.Contribution;
import com.supprojectstarter.beans.Group;
import com.supprojectstarter.beans.Project;
import com.supprojectstarter.beans.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.MalformedURLException;

public class JSONParser {
    static InputStream is = null;
    static String json = "";

    public JSONParser() {
    }

    public String makeGetRequest(String url) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getEntity() == null) {
                return null;
            } else {
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return json;
    }

    // POST REQUEST --> PROJECT
    public void makePostRequest(String url, Project project) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url);
            ObjectMapper mapper = new ObjectMapper();
            // Convert the project into json
            String jsonProject = mapper.writeValueAsString(project);
            StringEntity input = new StringEntity(jsonProject);
            input.setContentType("application/json");
            postRequest.setEntity(input);
            // Post to the rest service
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (response.getEntity().getContent())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // POST REQUEST --> GROUP
    public void makePostRequest(String url, Group group) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url);
            ObjectMapper mapper = new ObjectMapper();
            // Convert the project into json
            String jsonProject = mapper.writeValueAsString(group);
            StringEntity input = new StringEntity(jsonProject);
            input.setContentType("application/json");
            postRequest.setEntity(input);
            // Post to the rest service
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (response.getEntity().getContent())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // POST REQUEST --> USER
    public void makePostRequest(String url, User user) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url);
            ObjectMapper mapper = new ObjectMapper();
            // Convert the project into json
            String jsonProject = mapper.writeValueAsString(user);
            StringEntity input = new StringEntity(jsonProject);
            input.setContentType("application/json");
            postRequest.setEntity(input);
            // Post to the rest service
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (response.getEntity().getContent())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // PUT REQUEST --> USER
    public void makePutRequest(String url, User user) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPut putRequest = new HttpPut(url);
            ObjectMapper mapper = new ObjectMapper();
            // Convert the user into json
            String json = mapper.writeValueAsString(user);
            System.out.println(json);
            StringEntity input = new StringEntity(json);
            input.setContentType("application/json");
            putRequest.setEntity(input);
            // Post to the rest service
            HttpResponse response = httpClient.execute(putRequest);
            if (response.getStatusLine().getStatusCode() != 201 && response.getStatusLine().getStatusCode() != 204) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            httpClient.getConnectionManager().shutdown();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // POST REQUEST --> CONTRIBUTION
    public void makePostRequest(String url, Contribution contribution) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url);
            ObjectMapper mapper = new ObjectMapper();
            // Convert the contrib into json
            String json = mapper.writeValueAsString(contribution);
            StringEntity input = new StringEntity(json);
            input.setContentType("application/json");
            postRequest.setEntity(input);
            // Post to the rest service
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (response.getEntity().getContent())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
