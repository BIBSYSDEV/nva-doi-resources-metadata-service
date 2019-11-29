package no.bibsys.microservices.metadata.external.resource;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class Issued {
    @SerializedName(value = "date-parts")
    List<List<Integer>> date_parts;
}
