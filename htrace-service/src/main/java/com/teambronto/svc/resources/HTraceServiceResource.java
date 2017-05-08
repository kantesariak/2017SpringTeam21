package com.teambronto.svc.resources;

import com.github.kristofa.brave.Brave;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 
 * @author Sophia
 * The third micro-service that will access the Hbase database
 */
@Path("/htrace-svc")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HTraceServiceResource {
    private static final TableName EMPLOYEES_TABLENAME = TableName.valueOf("employees");
    private static final String PERSONAL_COL_FAMILY = "personal";

    private final Brave brave;
    private final Connection conn;

    public HTraceServiceResource(@NonNull Brave brave, @NotNull Connection conn) {
        this.brave = brave;
        this.conn = conn;
    }

    /**
     * Gets employee data for the specified employee ID.
     * @param rowId The employee's ID
     * @return The data associated with the ID specified (JSON).
     */
    @GET
    public String getEmployeeDataById(@DefaultValue("row1") @QueryParam("id") String rowId) {
        Get get = new Get(Bytes.toBytes(URLDecoder.decode(rowId)));
        get.addFamily(Bytes.toBytes(PERSONAL_COL_FAMILY));

        try {
            Table table = conn.getTable(EMPLOYEES_TABLENAME);

            Result result = table.get(get);
            byte[] firstName = result.getValue(Bytes.toBytes(PERSONAL_COL_FAMILY), Bytes.toBytes("first-name"));
            byte[] lastName  = result.getValue(Bytes.toBytes(PERSONAL_COL_FAMILY), Bytes.toBytes("last-name"));
            byte[] email     = result.getValue(Bytes.toBytes(PERSONAL_COL_FAMILY), Bytes.toBytes("email"));

            // Return JSON string representing an employee. Ideally this should be a Java object that is
            // serialized from JSON, but for the sake of the demo, we are just manually creating JSON here.
            return String.format("{\"employee\":{\"firstName\":\"%s\",\"lastName\":\"%s\",\"email\":\"%s\",\"rowId\":\"%s\"}}",
                    Bytes.toString(firstName),
                    Bytes.toString(lastName),
                    Bytes.toString(email),
                    rowId);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
    
    

    /**
     * Creates a new table caled 'employees'. In the future, you could add a query parameter to allow
     * the user to specify a table name to use. The table has a column family name called 'personal'.
     *
     * @return A message indicating success or failure.
     */
    @GET
    @Path("/create-table")
    public String createTable(@DefaultValue("employees") @QueryParam("name") String name) throws IOException {
    	Admin admin = conn.getAdmin();
    	
    	TableName tn = TableName.valueOf(URLDecoder.decode(name));
    	       
        if (!admin.tableExists(tn)) {
          final HTableDescriptor tableDescriptor = new HTableDescriptor("employees");
          tableDescriptor.addFamily(new HColumnDescriptor(PERSONAL_COL_FAMILY));

          admin.createTable(tableDescriptor);
          
          return String.format("Creating table employees");
        } else {
          return String.format("Found table employees" );
        }

    }

    /**
     * Deletes a table with the specified name.
     * @param name The table's name
     * @return Success or failure message
     * @throws IOException If HBase can't be reached.
     */
    @GET
    @Path("/delete-table")
    public String deleteTable(@DefaultValue("employees") @QueryParam("name") String name) throws IOException {
    	String tableName = URLDecoder.decode(name);
    	Admin admin = conn.getAdmin();
        // closing the HTable object
    	if(admin.tableExists(TableName.valueOf(tableName))){
    		admin.disableTable(TableName.valueOf(tableName));
    		admin.deleteTable(TableName.valueOf(tableName));
    		return String.format("delete: " + name);
    	} else{
    		return String.format( name + " does not exist ");
    	}
     }

    /**
     * Updates employee information in the database.
     * @param row The employee's row id
     * @param first the first name
     * @param last the last name
     * @param email the email address
     * @return Success or failure mesage
     * @throws IOException If HBase cannot be reached for some reason
     */
    @GET
    @Path("/update-value")
    public String updateValue(@NotNull @QueryParam("row") String row,
                              @NotNull @QueryParam("first") String first,
                              @NotNull @QueryParam("last") String last,
                              @NotNull @QueryParam("email") String email) throws IOException {
    	String tableRow  = URLDecoder.decode(row);
    	String firstName = URLDecoder.decode(first);
    	String lastName  = URLDecoder.decode(last);
    	String emailAddr = URLDecoder.decode(email);

    	Table table = conn.getTable(EMPLOYEES_TABLENAME);

        Put p = new Put(Bytes.toBytes(tableRow));
        p.add(Bytes.toBytes(PERSONAL_COL_FAMILY),Bytes.toBytes("first-name"),Bytes.toBytes(firstName));
        p.add(Bytes.toBytes(PERSONAL_COL_FAMILY),Bytes.toBytes("last-name"),Bytes.toBytes(lastName));
        p.add(Bytes.toBytes(PERSONAL_COL_FAMILY),Bytes.toBytes("email"),Bytes.toBytes(emailAddr));
        
        // Saving the put Instance to the HTable.
        table.put(p);
        return String.format("data Updated");
        //table.close();

     }
    
    
}
