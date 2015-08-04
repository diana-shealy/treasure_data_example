package main;

import org.apache.commons.cli.*;

public class TreasureDataCLIParser {
    private String[] args = null;
    private Options tdOptions = new Options();

    public TreasureDataCLIParser(String[] args) {

        this.args = args;

        tdOptions.addOption("db","db_name", true, "Database Name - REQUIRED");
        tdOptions.addOption("tb", "table_name", true, "Table Name - REQUIRED");
        tdOptions.addOption("c", "column", true, "Comma separated list of columns to be selected - Optional");
        tdOptions.addOption("m", "min", true, "Minimum timestamp - NULL by default - Optional");
        tdOptions.addOption("M", "MAX", true, "Maximum timestamp - NULL by default - Optional");
        tdOptions.addOption("e", "engine", true, "Treasure Data Query Engine - Presto by default - Optional");
        tdOptions.addOption("f", "format", true, "Return File Format - tabular by default - Optional");
        tdOptions.addOption("l", "limit", true, "Query Results Limit - Optional");

    }

    public TreasureDataQuery parse() {
        CommandLineParser parser = new DefaultParser();
        String database = "";
        String table = "";
        String format = "tabular";
        String engine = "presto";
        String limit = "NULL";
        String min_time = "NULL";
        String max_time = "NULL";
        String columns = "*";

        CommandLine cmd;
        try {
            cmd = parser.parse(tdOptions, args);

            if (cmd.hasOption("db") && cmd.getOptionValue("db") != null) {
                database = cmd.getOptionValue("db");
            }
            else {
                throw new ParseException("No database selected, this is a required field. Refer to usage below.");
            }

            if (cmd.hasOption("tb") && cmd.getOptionValue("tb") != null) {
                table = cmd.getOptionValue("tb");
            }
            else {
                throw new ParseException("No table selected, this is a required field. Refer to usage below.");
            }

            if (cmd.hasOption("c")) {
                if (cmd.getOptionValue("c") != null) {
                    columns = cmd.getOptionValue("c");
                }
                else {
                    throw new ParseException("Columns option selected but no columns defined. Refer to usage below.");
                }
            }

            if (cmd.hasOption("m")) {
                if (cmd.getOptionValue("m") != null) {
                    min_time = cmd.getOptionValue("m");
                }
                else {
                    throw new ParseException("Min_time option selected but not defined. Refer to usage below.");
                }
            }

            if (cmd.hasOption("M")) {
                if (cmd.getOptionValue("M") != null) {
                    max_time = cmd.getOptionValue("M");
                }
                else {
                    throw new ParseException("Max_time option selected but not defined. Refer to usage below.");
                }
            }

            if (cmd.hasOption("e")) {
                if (cmd.getOptionValue("e") != null) {
                    engine = cmd.getOptionValue("e");
                }
                else {
                    throw new ParseException("Engine option selected but not defined. Refer to usage below.");
                }
            }

            if (cmd.hasOption("f")) {
                if (cmd.getOptionValue("f") != null) {
                    format = cmd.getOptionValue("f");
                }
                else {
                    throw new ParseException("Format option selected but not defined. Refer to usage below.");
                }
            }

            if (cmd.hasOption("l")) {
                if (cmd.getOptionValue("l") != null) {
                    limit = cmd.getOptionValue("l");
                }
                else {
                    throw new ParseException("Limit option selected but not defined. Refer to usage below.");
                }
            }

        } catch (ParseException e) {
            System.err.println(e.getMessage());
            help();
        }

        return new TreasureDataQuery(database, table, format, limit, columns, engine, min_time, max_time);
    }

    private void help() {
        HelpFormatter formatHelp = new HelpFormatter();

        formatHelp.printHelp("Main", tdOptions);
        System.exit(0);
    }
}
