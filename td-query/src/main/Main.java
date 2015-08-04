package main;

import com.treasure_data.client.ClientException;

public class Main {

    public static void main(String[] args) {

        try {
            Query query = new Query();
            query.doApp();
        } catch (ClientException e) {
            System.out.print("Application Failed:" + e.getLocalizedMessage());
        }
    }

}
