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

    public void runQuery(TreasureDataQuery td_query) throws ClientException {
        TreasureDataClient client = new TreasureDataClient();

        Job job = new Job(new Database(td_query.getDBName()), td_query.buildTDQueryString(td_query));
        if (td_query.getEngine().equals("hive")) {
            job.setType(Job.Type.HIVE);
        }
        else {
            job.setType(Job.Type.PRESTO);
        }
        client.submitJob(job);
        String jobID = job.getJobID();
        System.out.println("Initializing...\n JobID: " + jobID);
        System.out.println("Running query: " + td_query.buildTDQueryString(td_query) + "\n\n");

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
                System.err.println("Application Timed Out. Please check your settings and try again.");
                break;
            }
        }

        JobResult jobResult = client.getJobResult(job);
        Unpacker unpacker = jobResult.getResult(); // Unpacker class is MessagePack's deserializer
        UnpackerIterator iter = unpacker.iterator();
        if (!td_query.getColumn().equals("*")) {
            System.out.println(td_query.getColumn() + "\n-----------------");
        }

        while (iter.hasNext()) {
            ArrayValue row = iter.next().asArrayValue();
            for (Value elm : row) {
                if (td_query.getFormat().equals("csv")) {
                    System.out.print(elm + ",");
                }
                else {
                    System.out.print(elm + "\t");
                }
            }
            System.out.println();
        }

        if (!td_query.getLimit().equals("NULL") && jobResult.getResultSize() > Long.parseLong(td_query.getLimit())) {
            System.out.println(td_query.getLimit() + " Results Returned\n\n");
        }
        else {
            System.out.println(jobResult.getResultSize() + " Results Returned\n\n");
        }
    }

}
