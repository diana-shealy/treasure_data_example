package main;

import java.io.IOException;
import java.util.Properties;

import org.msgpack.type.ArrayValue;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Unpacker;
import org.msgpack.unpacker.UnpackerIterator;

import com.treasure_data.client.ClientException;
import com.treasure_data.client.TreasureDataClient;
import com.treasure_data.model.Database;
import com.treasure_data.model.Job;
import com.treasure_data.model.JobResult;
import com.treasure_data.model.JobSummary;

public class Query {
    static {
        try {
            Properties props = System.getProperties();
            props.load(Query.class.getClassLoader().getResourceAsStream("treasure-data.properties"));
        } catch (IOException e) {
            System.out.print("Log In Failed" + e.getLocalizedMessage());
        }
    }

    /*
    public void doApp() throws ClientException {
        TreasureDataClient client = new TreasureDataClient();

        List<DatabaseSummary> databases = client.listDatabases();
        for (DatabaseSummary database : databases) {
            String databaseName = database.getName();
            List<TableSummary> tables = client.listTables(databaseName);
            for (TableSummary table : tables) {
                System.out.println(databaseName);
                System.out.println(table.getName());
                System.out.println(table.getCount());
            }
        }
    }*/

    public void doApp() throws ClientException {
        TreasureDataClient client = new TreasureDataClient();

        Job job = new Job(new Database("amazon_security_sample"), "SELECT target_name, COUNT(DISTINCT login) FROM amazon_access GROUP BY target_name");
        //client.submitJob(job);
        client.submitJob(job);
        String jobID = job.getJobID();
        System.out.println("Initializing...\n JobID: " + jobID);

        while (true) {
            JobSummary.Status stat = client.showJobStatus(job);
            if (stat == JobSummary.Status.SUCCESS) {
                break;
            } else if (stat == JobSummary.Status.ERROR) {
                String msg = String.format("Job '%s' failed: got Job status 'error'", jobID);
                JobSummary js = client.showJob(job);
                if (js.getDebug() != null) {
                    System.out.println("cmdout:");
                    System.out.println(js.getDebug().getCmdout());
                    System.out.println("stderr:");
                    System.out.println(js.getDebug().getStderr());
                }
                throw new ClientException(msg);
            } else if (stat == JobSummary.Status.KILLED) {
                String msg = String.format("Job '%s' failed: got Job status 'killed'", jobID);
                throw new ClientException(msg);
            }

            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                // do something
            }
        }

        JobResult jobResult = client.getJobResult(job);
        Unpacker unpacker = jobResult.getResult(); // Unpacker class is MessagePack's deserializer
        UnpackerIterator iter = unpacker.iterator();
        while (iter.hasNext()) {
            ArrayValue row = iter.next().asArrayValue();
            for (Value elm : row) {
                System.out.print(elm + "\t");
            }
            System.out.println();
        }
    }
}
