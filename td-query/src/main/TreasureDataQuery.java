package main;

public class TreasureDataQuery {

    private String db_name = "";
    private String table_name = "";
    private String format = "tabular"; //default to tabular
    private String limit = "NULL";
    private String column = "*";      //default to all columns
    private String engine = "presto"; //default to presto
    private String min_date = "NULL"; //default to NULL
    private String max_date = "NULL"; //default to NULL

    public TreasureDataQuery(String db_name, String table_name) {
        this.db_name = db_name;
        this.table_name = table_name;
    }

    public TreasureDataQuery(String db_name, String table_name, String format, String limit, String column, String engine,
                             String min_date, String max_date) {
        this.db_name = db_name;
        this.table_name = table_name;
        this.format = format;
        this.limit = limit;
        this.column = column;
        this.engine = engine;
        this.min_date = min_date;
        this.max_date = max_date;
    }

    public String getDBName() {
        return this.db_name;
    }

    public String getTableName() {
        return this.table_name;
    }

    public String getFormat() {
        return this.format;
    }

    public String getLimit() {
        return this.limit;
    }

    public String getColumn() {
        return this.column;
    }

    public String getEngine() {
        return this.engine;
    }

    public String getMinDate() {
        return this.min_date;
    }

    public String getMaxDate() {
        return this.max_date;
    }

    public String buildTDQueryString(TreasureDataQuery query_info) {

        //Build basic query
        String query = "SELECT " + query_info.getColumn() + " FROM " + query_info.getTableName();

        // If at least one date range given, build WHERE clause
        if (!query_info.getMinDate().equals("NULL") || !query_info.getMaxDate().equals("NULL")) {
            query = query + " WHERE TD_TIME_RANGE(time, " + query_info.getMinDate() + ", " + query_info.getMaxDate() + ")";
        }

        // If a limit given, build LIMIT clause
        if (!query_info.getLimit().equals("NULL")) {
            query = query + " LIMIT " + query_info.getLimit();
        }

        return query;

    }



}
