package com.supprojectstarter.beans;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@idContrib")
public class Contribution {
    private int id;
    private User contributorUser;
    private Project project;
    private int amountC;

    public int getAmountC() {
        return amountC;
    }

    public void setAmountC(int amount) {
        this.amountC = amount;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getContributorUser() {
        return contributorUser;
    }

    public void setContributorUser(User contributorUser) {
        this.contributorUser = contributorUser;
    }
}
