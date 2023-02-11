package searchengine.config;


import lombok.Data;



@Data
public class Search {
    private String query;
    private String site;
    private int offset;
    private int limit;
}
