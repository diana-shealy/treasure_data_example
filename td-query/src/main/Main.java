package main;

import com.treasure_data.client.ClientException;

import org.apache.commons.cli.*;

import java.io.OutputStream;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {

        String database = "amazon_security_sample";
        String table = "amazon_access";
        String format = "csv";
        String engine = "hive";
        String limit = "10";
        String min_time = "2010-08-31";
        String max_time = "2010-09-01";
        String columns = "target_name,action,login";

        // Example
        TreasureDataQuery query_definition = new TreasureDataQuery(database, table,format,limit, columns, engine, min_time, max_time);

        try {
            Query query = new Query();
            query.runQuery(query_definition);
        } catch (ClientException e) {
            System.out.print("Application Failed:" + e.getLocalizedMessage());
        }
    }

    public static Options constructTDOptions()
    {
        final Options tdOptions = new Options();
        tdOptions.addOption("db","db_name", true, "Database Name")
                .addOption("table","table_name", true, "Database Table")
                .addOption("c", "column", true, "List of columns to be selected - comma separated")
                .addOption("m","min", true, "Minimum timestamp - NULL by default")
                .addOption("M", "MAX", true, "Maximum timestamp - NULL by default")
                .addOption("e", "engine", true, "Treasure Data Query Engine - Presto by default")
                .addOption("f", "format", true, "Return File Format - tabular by default")
                .addOption("l", "limit", true, "Query Results Limit");
        return tdOptions;
    }

    public static void printUsage(final String applicationName, final Options options, final OutputStream out) {
        final PrintWriter writer = new PrintWriter(out);
        final HelpFormatter usageFormatter = new HelpFormatter();
        usageFormatter.printUsage(writer, 80, applicationName, options);
        writer.close();
    }

    public static void printHelp(final Options options, final int printedRowWidth, final String header,
                                 final String footer, final int spacesBeforeOption, final int spacesBeforeOptionDescription,
                                 final boolean displayUsage, final OutputStream out) {

        final String commandLineSyntax = "query -f csv -e hive -c 'my_col1,my_col2,my_col5' \n" +
                "        -m 1427347140 -M 1427350725 -l 100 \n" +
                "\t  -db my_db -table my_table";
        final PrintWriter writer = new PrintWriter(out);
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(
                writer,
                printedRowWidth,
                commandLineSyntax,
                header,
                options,
                spacesBeforeOption,
                spacesBeforeOptionDescription,
                footer,
                displayUsage);
        writer.close();
    }

    public static void useTDParser(final String[] commandLineArguments)
    {
        final CommandLineParser cmdLineTDParser = new DefaultParser();

        final Options tdOptions = constructTDOptions();
        CommandLine commandLine;
        try
        {
            commandLine = cmdLineTDParser.parse(tdOptions, commandLineArguments);
            if ( commandLine.hasOption("db") )
            {
                System.out.println("Name of the Treasure Data ");
            }
            if ( commandLine.hasOption("print") )
            {
                System.out.println("You want to print (print chosen)!");
            }
            if ( commandLine.hasOption('g') )
            {
                System.out.println("You want a GUI!");
            }
            if ( commandLine.hasOption("n") )
            {
                System.out.println(
                        "You selected the number " + commandLine.getOptionValue("n"));
            }
        }
        catch (ParseException parseException)  // checked exception
        {
            System.err.println(
                    "Encountered exception while parsing using GnuParser:\n"
                            + parseException.getMessage() );
        }
    }

}
