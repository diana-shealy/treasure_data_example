package main;

import com.treasure_data.client.ClientException;

public class Main {

    public static void main(String[] args) {

        //Parse command line
        try {
            TreasureDataCLIParser td_parser = new TreasureDataCLIParser(args);
            TreasureDataQuery testCLI = td_parser.parse();
            Query query = new Query();
            query.runQuery(testCLI);
        } catch (ClientException e) {
            System.err.print("Application Failed:" + e.getLocalizedMessage());
        }
    }
}
