package com.supprojectstarter.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@idProject")
public class Project {
    private int id;
    private String name;
    private List<Contribution> contributions;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private DateTime creationDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private DateTime startDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private DateTime endDate;
    private int amountP;
    private String description;
    private User creatorUser;
    private Category category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.print(startDate);
    }

    public void setStartDate(String startDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        DateTime dt = formatter.parseDateTime(startDate);
        this.startDate = dt;
    }

    public DateTime StartDateTime() {
        return startDate;
    }

    public void StartDateWithDateTime(DateTime startDate) {
        this.startDate = startDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getEndDate() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.print(endDate);
    }

    public void setEndDate(String endDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        DateTime dt = formatter.parseDateTime(endDate);
        this.endDate = dt;
    }

    public DateTime EndDate() {
        return endDate;
    }

    public void EndDateWithDateTime(DateTime endDate) {
        this.endDate = endDate;
    }

    public int getAmountP() {
        return amountP;
    }

    public void setAmountP(int amount) {
        this.amountP = amount;
    }

    public User getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(User creatorUser) {
        this.creatorUser = creatorUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Project [id=" + id + ", name=" + name + ", creationDate="
                + startDate + ", completionDate=" + endDate
                + ", creator=" + creatorUser + ", category=" + category + "]";
    }

    public List<Contribution> getContributions() {
        return contributions;
    }

    public void setContributions(List<Contribution> contributions) {
        this.contributions = contributions;
    }

    public String getCreationDate() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.print(creationDate);
    }

    public void setCreationDate(String creationDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        DateTime dt = formatter.parseDateTime(creationDate);
        this.creationDate = dt;
    }

    public DateTime CreationDate() {
        return creationDate;
    }

    public void CreationDateWithDateTime(DateTime creationDate) {
        this.creationDate = creationDate;
    }
}
