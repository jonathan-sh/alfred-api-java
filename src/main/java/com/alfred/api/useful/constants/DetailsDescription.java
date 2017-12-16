package com.alfred.api.useful.constants;

/**
 * Created by jonathan on 7/15/17.
 */
public enum DetailsDescription {

    FIELDS_REQUIRED("You must inform these valid fields : "),
    PASSWORD("you shall not pass"),
    ALREADY_EXISTS("A record already exists for : "),
    NOT_FOUND("Record not found with this email : "),
    NOT_PUSH("Not is a webhook type [ push ]"),
    NOT_CONTAINS_MACHINE_NAME("Not contains machine name"),
    NOT_CONTAINS_MACHINE("Not contains machine"),
    NOT_CONTAINS_VALID_MACHINE("Not contains valid machine"),
    NOT_CONTAINS_VALID_APPLICATION("Not contains valid application"),
    NOT_CONTAINS_ACTIVE_MACHINE("Not contains active machine"),
    NOT_CONTAINS_BRANCH_("Not contains branch"),
    NOT_CONTAINS_VALID_BRANCH("Not contains valid branch"),
    NOT_CONTAINS_REPOSITORY("Not contains repository valid");

    private String description;

    DetailsDescription(String label) {
        this.description = label;
    }

    public String get() {
        return this.description;
    }
}
