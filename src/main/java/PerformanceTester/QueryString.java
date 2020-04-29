package PerformanceTester;

public class QueryString {
   private String query, viewname;

    public QueryString(String query, String viewname) {
        this.query = query;
        this.viewname = viewname;
    }

    public String getQuery() {
        return query;
    }

    public String getViewname() {
        return viewname;
    }

    @Override
    public String toString() {
        return query + viewname;
    }
}
