package com.teambronto.svc;

import com.github.kristofa.brave.Brave;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang.NullArgumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.security.SecurityCapability;
import org.apache.hadoop.hbase.ipc.CoprocessorRpcChannel;
import org.apache.hadoop.hbase.protobuf.generated.AdminProtos;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos;
import org.apache.hadoop.hbase.protobuf.generated.MasterProtos;
import org.apache.hadoop.hbase.quotas.QuotaFilter;
import org.apache.hadoop.hbase.quotas.QuotaRetriever;
import org.apache.hadoop.hbase.quotas.QuotaSettings;
import org.apache.hadoop.hbase.regionserver.wal.FailedLogCloseException;
import org.apache.hadoop.hbase.snapshot.HBaseSnapshotException;
import org.apache.hadoop.hbase.snapshot.RestoreSnapshotException;
import org.apache.hadoop.hbase.snapshot.SnapshotCreationException;
import org.apache.hadoop.hbase.snapshot.UnknownSnapshotException;
import org.apache.hadoop.hbase.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
 * TracedAdmin adds tracing to all of the Admin interface's methods. Having the tracing done through this
 * delegate wrapper allows the tracing to be performed behind the scenes. Whenever a user interacts with a
 * TracedAdmin object just like they would with an Admin object, Zipkin tracing is performed.
 *
 * Note: A good goal would be to use Java's Proxy API to do this cleaner with less code repetition.
 */
public class TracedAdmin implements Admin {
    /** The delegate that will implement behavior */
    private Admin delegate;
    /** An instance of Brave used for tracing */
    private Brave brave;
    
    /**
     * Gets the method name by looking into the stacktrace
     * Very inefficient in terms of performance, TODO: change to string literal for each function
     * @return current HBase MethodName
     */
    @Deprecated
    private String getMethodName(){
    	return Thread.currentThread().getStackTrace()[2].getMethodName();

    }
    
    public TracedAdmin(@NonNull Admin delegate, @NonNull Brave brave) {
        this.delegate = delegate;
        this.brave = brave;
    }

    @Override
    public int getOperationTimeout() {
        brave.localTracer().startNewSpan("admin", getMethodName());
        int res = delegate.getOperationTimeout();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public void abort(String s, Throwable throwable) {
        brave.localTracer().startNewSpan("admin", getMethodName());
        delegate.abort(s, throwable);
        brave.localTracer().finishSpan();
    }

    @Override
    public boolean isAborted() {
        brave.localTracer().startNewSpan("admin", getMethodName());
        boolean res = delegate.isAborted();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public Connection getConnection() {
    	
    	return delegate.getConnection();
        
    }

    @Override
    public boolean tableExists(TableName tableName) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        boolean res = delegate.tableExists(tableName);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public HTableDescriptor[] listTables() throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        HTableDescriptor[] res = delegate.listTables();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public HTableDescriptor[] listTables(Pattern pattern) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        HTableDescriptor[] res = delegate.listTables(pattern);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public HTableDescriptor[] listTables(String s) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        HTableDescriptor[] res = delegate.listTables(s);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public HTableDescriptor[] listTables(Pattern pattern, boolean b) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        HTableDescriptor[] res = delegate.listTables(pattern, b);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public HTableDescriptor[] listTables(String s, boolean b) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        HTableDescriptor[] res = delegate.listTables(s, b);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public TableName[] listTableNames() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
        TableName[] res = delegate.listTableNames();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public TableName[] listTableNames(Pattern pattern) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        TableName[] res = delegate.listTableNames(pattern);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public TableName[] listTableNames(String s) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        TableName[] res = delegate.listTableNames(s);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public TableName[] listTableNames(Pattern pattern, boolean b) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        TableName[] res = delegate.listTableNames(pattern, b);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public TableName[] listTableNames(String s, boolean b) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        TableName[] res = delegate.listTableNames(s, b);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public HTableDescriptor getTableDescriptor(TableName tableName) throws TableNotFoundException, IOException {
    	HTableDescriptor res = new HTableDescriptor();
        brave.localTracer().startNewSpan("admin", getMethodName());
        res = delegate.getTableDescriptor(tableName);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public void createTable(HTableDescriptor hTableDescriptor) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        delegate.createTable(hTableDescriptor);
        brave.localTracer().finishSpan();
    }

    @Override
    public void createTable(HTableDescriptor hTableDescriptor, byte[] bytes, byte[] bytes1, int i) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        delegate.createTable(hTableDescriptor, bytes, bytes1, i);
        brave.localTracer().finishSpan();
    }

    @Override
    public void createTable(HTableDescriptor hTableDescriptor, byte[][] bytes) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        delegate.createTable(hTableDescriptor, bytes);
        brave.localTracer().finishSpan();
    }

    @Override
    public void createTableAsync(HTableDescriptor hTableDescriptor, byte[][] bytes) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        delegate.createTableAsync(hTableDescriptor, bytes);
        brave.localTracer().finishSpan();
    }

    @Override
    public void deleteTable(TableName tableName) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        delegate.deleteTable(tableName);
        brave.localTracer().finishSpan();
    }

    @Override
    public HTableDescriptor[] deleteTables(String s) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        HTableDescriptor[] res = delegate.deleteTables(s);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public HTableDescriptor[] deleteTables(Pattern pattern) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        HTableDescriptor[] res = delegate.deleteTables(pattern);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public void truncateTable(TableName tableName, boolean b) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        delegate.truncateTable(tableName, b);
        brave.localTracer().finishSpan();
    }

    @Override
    public void enableTable(TableName tableName) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        delegate.enableTable(tableName);
        brave.localTracer().finishSpan();
    }

    @Override
    public void enableTableAsync(TableName tableName) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        delegate.enableTableAsync(tableName);
        brave.localTracer().finishSpan();
    }

    @Override
    public HTableDescriptor[] enableTables(String s) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        HTableDescriptor[] res = delegate.enableTables(s);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public HTableDescriptor[] enableTables(Pattern pattern) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        HTableDescriptor[] res = delegate.enableTables(pattern);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public void disableTableAsync(TableName tableName) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        delegate.disableTableAsync(tableName);
        brave.localTracer().finishSpan();
    }

    @Override
    public void disableTable(TableName tableName) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        delegate.disableTable(tableName);
        brave.localTracer().finishSpan();
    }

    @Override
    public HTableDescriptor[] disableTables(String s) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        HTableDescriptor[] res = delegate.disableTables(s);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public HTableDescriptor[] disableTables(Pattern pattern) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        HTableDescriptor[] res = delegate.disableTables(pattern);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean isTableEnabled(TableName tableName) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        boolean res = delegate.isTableEnabled(tableName);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean isTableDisabled(TableName tableName) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        boolean res = delegate.isTableDisabled(tableName);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean isTableAvailable(TableName tableName) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        boolean res = delegate.isTableAvailable(tableName);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean isTableAvailable(TableName tableName, byte[][] bytes) throws IOException {
        brave.localTracer().startNewSpan("admin", getMethodName());
        boolean res = delegate.isTableAvailable(tableName, bytes);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public Pair<Integer, Integer> getAlterStatus(TableName tableName) throws IOException {
    	Pair<Integer, Integer> res = new Pair<Integer, Integer>();
        brave.localTracer().startNewSpan("admin", getMethodName());
        res = delegate.getAlterStatus(tableName);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public Pair<Integer, Integer> getAlterStatus(byte[] bytes) throws IOException {
    	Pair<Integer, Integer> res = new Pair<Integer, Integer>();
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	res = delegate.getAlterStatus(bytes);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public void addColumn(TableName tableName, HColumnDescriptor hColumnDescriptor) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.addColumn(tableName, hColumnDescriptor);
        brave.localTracer().finishSpan();
    }

    @Override
    public void deleteColumn(TableName tableName, byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.deleteColumn(tableName, bytes);
        brave.localTracer().finishSpan();

    }

    @Override
    public void modifyColumn(TableName tableName, HColumnDescriptor hColumnDescriptor) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.modifyColumn(tableName, hColumnDescriptor);
        brave.localTracer().finishSpan();
    }

    @Override
    public void closeRegion(String s, String s1) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.closeRegion(s, s1);
        brave.localTracer().finishSpan();
    }

    @Override
    public void closeRegion(byte[] bytes, String s) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.closeRegion(bytes, s);
        brave.localTracer().finishSpan();
    }

    @Override
    public boolean closeRegionWithEncodedRegionName(String s, String s1) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.closeRegionWithEncodedRegionName(s, s1);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public void closeRegion(ServerName serverName, HRegionInfo hRegionInfo) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.closeRegion(serverName, hRegionInfo);
        brave.localTracer().finishSpan();
    }

    @Override
    public List<HRegionInfo> getOnlineRegions(ServerName serverName) throws IOException {
        return delegate.getOnlineRegions(serverName);
    }

    @Override
    public void flush(TableName tableName) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.flush(tableName);
        brave.localTracer().finishSpan();
    }

    @Override
    public void flushRegion(byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.flushRegion(bytes);
        brave.localTracer().finishSpan();

    }

    @Override
    public void compact(TableName tableName) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.compact(tableName);
        brave.localTracer().finishSpan();

    }

    @Override
    public void compactRegion(byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.compactRegion(bytes);
        brave.localTracer().finishSpan();

    }

    @Override
    public void compact(TableName tableName, byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.compact(tableName, bytes);
        brave.localTracer().finishSpan();
    }

    @Override
    public void compactRegion(byte[] bytes, byte[] bytes1) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.compactRegion(bytes, bytes1);
        brave.localTracer().finishSpan();
    }

    @Override
    public void majorCompact(TableName tableName) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.majorCompact(tableName);
        brave.localTracer().finishSpan();
    }

    @Override
    public void majorCompactRegion(byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.majorCompactRegion(bytes);
        brave.localTracer().finishSpan();
    }

    @Override
    public void majorCompact(TableName tableName, byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.majorCompact(tableName, bytes);
        brave.localTracer().finishSpan();
    }

    @Override
    public void majorCompactRegion(byte[] bytes, byte[] bytes1) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.majorCompactRegion(bytes, bytes1);
        brave.localTracer().finishSpan();
    }

    @Override
    public void compactRegionServer(ServerName serverName, boolean b) throws IOException, InterruptedException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.compactRegionServer(serverName, b);
        brave.localTracer().finishSpan();
    }

    @Override
    public void move(byte[] bytes, byte[] bytes1) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.move(bytes, bytes1);
        brave.localTracer().finishSpan();
    }

    @Override
    public void assign(byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.assign(bytes);
        brave.localTracer().finishSpan();
    }

    @Override
    public void unassign(byte[] bytes, boolean b) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.unassign(bytes, b);
        brave.localTracer().finishSpan();
    }

    @Override
    public void offline(byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.offline(bytes);
        brave.localTracer().finishSpan();
    }

    @Override
    public boolean setBalancerRunning(boolean b, boolean b1) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.setBalancerRunning(b, b1);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean balancer() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.balancer();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean balancer(boolean b) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.balancer(b);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean isBalancerEnabled() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.isBalancerEnabled();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean normalize() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.normalize();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean isNormalizerEnabled() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.isNormalizerEnabled();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean setNormalizerRunning(boolean b) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.setNormalizerRunning(b);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean enableCatalogJanitor(boolean b) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.enableCatalogJanitor(b);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public int runCatalogScan() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	int res = delegate.runCatalogScan();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean isCatalogJanitorEnabled() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.isCatalogJanitorEnabled();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public void mergeRegions(byte[] bytes, byte[] bytes1, boolean b) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.mergeRegions(bytes, bytes1, b);
        brave.localTracer().finishSpan();
    }

    @Override
    public void split(TableName tableName) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.split(tableName);
        brave.localTracer().finishSpan();

    }

    @Override
    public void splitRegion(byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.splitRegion(bytes);
        brave.localTracer().finishSpan();

    }

    @Override
    public void split(TableName tableName, byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.split(tableName, bytes);
        brave.localTracer().finishSpan();

    }

    @Override
    public void splitRegion(byte[] bytes, byte[] bytes1) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.splitRegion(bytes, bytes1);
        brave.localTracer().finishSpan();


    }

    @Override
    public void modifyTable(TableName tableName, HTableDescriptor hTableDescriptor) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.modifyTable(tableName, hTableDescriptor);
        brave.localTracer().finishSpan();
    }

    @Override
    public void shutdown() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.shutdown();
        brave.localTracer().finishSpan();
    }

    @Override
    public void stopMaster() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.stopMaster();
        brave.localTracer().finishSpan();

    }

    @Override
    public void stopRegionServer(String s) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.stopRegionServer(s);
        brave.localTracer().finishSpan();

    }

    @Override
    public ClusterStatus getClusterStatus() throws IOException {
    	return delegate.getClusterStatus();
    }

    @Override
    public Configuration getConfiguration() {
    	return delegate.getConfiguration();
    }

    @Override
    public void createNamespace(NamespaceDescriptor namespaceDescriptor) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.createNamespace(namespaceDescriptor);
        brave.localTracer().finishSpan();
    }

    @Override
    public void modifyNamespace(NamespaceDescriptor namespaceDescriptor) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.modifyNamespace(namespaceDescriptor);
        brave.localTracer().finishSpan();
    }

    @Override
    public void deleteNamespace(String s) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.deleteNamespace(s);
        brave.localTracer().finishSpan();
    }

    @Override
    public NamespaceDescriptor getNamespaceDescriptor(String s) throws NamespaceNotFoundException, IOException {
    	return delegate.getNamespaceDescriptor(s);
    }

    @Override
    public NamespaceDescriptor[] listNamespaceDescriptors() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	NamespaceDescriptor[] res = delegate.listNamespaceDescriptors();
        brave.localTracer().finishSpan();
    	
        return res;
    }

    @Override
    public HTableDescriptor[] listTableDescriptorsByNamespace(String s) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	HTableDescriptor[] res = delegate.listTableDescriptorsByNamespace(s);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public TableName[] listTableNamesByNamespace(String s) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	TableName[] res = delegate.listTableNamesByNamespace(s);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public List<HRegionInfo> getTableRegions(TableName tableName) throws IOException {
    	return delegate.getTableRegions(tableName);
    }

    @Override
    public void close() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.close();
        brave.localTracer().finishSpan();

    }

    @Override
    public HTableDescriptor[] getTableDescriptorsByTableName(List<TableName> list) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	HTableDescriptor[] res = delegate.getTableDescriptorsByTableName(list);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public HTableDescriptor[] getTableDescriptors(List<String> list) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	HTableDescriptor[] res = delegate.getTableDescriptors(list);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean abortProcedure(long l, boolean b) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.abortProcedure(l, b);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public ProcedureInfo[] listProcedures() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	ProcedureInfo[] res = delegate.listProcedures();
        brave.localTracer().finishSpan();
        
        return res;
    }

    @Override
    public Future<Boolean> abortProcedureAsync(long l, boolean b) throws IOException {
    	return delegate.abortProcedureAsync(l, b);
        
    }

    @Override
    public void rollWALWriter(ServerName serverName) throws IOException, FailedLogCloseException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.rollWALWriter(serverName);
        brave.localTracer().finishSpan();
    }

    @Override
    public String[] getMasterCoprocessors() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	String[] res = delegate.getMasterCoprocessors();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public AdminProtos.GetRegionInfoResponse.CompactionState getCompactionState(TableName tableName) throws IOException {
        return delegate.getCompactionState(tableName);
    }

    @Override
    public AdminProtos.GetRegionInfoResponse.CompactionState getCompactionStateForRegion(byte[] bytes) throws IOException {
        return delegate.getCompactionStateForRegion(bytes);
    }

    @Override
    public long getLastMajorCompactionTimestamp(TableName tableName) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	Long res = delegate.getLastMajorCompactionTimestamp(tableName);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public long getLastMajorCompactionTimestampForRegion(byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	Long res = delegate.getLastMajorCompactionTimestampForRegion(bytes);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public void snapshot(String s, TableName tableName) throws IOException, SnapshotCreationException, IllegalArgumentException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.snapshot(s, tableName);
        brave.localTracer().finishSpan();
    }

    @Override
    public void snapshot(byte[] bytes, TableName tableName) throws IOException, SnapshotCreationException, IllegalArgumentException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.snapshot(bytes, tableName);
        brave.localTracer().finishSpan();
    }

    @Override
    public void snapshot(String s, TableName tableName, HBaseProtos.SnapshotDescription.Type type) throws IOException, SnapshotCreationException, IllegalArgumentException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.snapshot(s, tableName, type);
        brave.localTracer().finishSpan();
    }

    @Override
    public void snapshot(HBaseProtos.SnapshotDescription snapshotDescription) throws IOException, SnapshotCreationException, IllegalArgumentException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.snapshot(snapshotDescription);
        brave.localTracer().finishSpan();
    }

    @Override
    public MasterProtos.SnapshotResponse takeSnapshotAsync(HBaseProtos.SnapshotDescription snapshotDescription) throws IOException, SnapshotCreationException {
        return delegate.takeSnapshotAsync(snapshotDescription);
    }

    @Override
    public boolean isSnapshotFinished(HBaseProtos.SnapshotDescription snapshotDescription) throws IOException, HBaseSnapshotException, UnknownSnapshotException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.isSnapshotFinished(snapshotDescription);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public void restoreSnapshot(byte[] bytes) throws IOException, RestoreSnapshotException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.restoreSnapshot(bytes);
        brave.localTracer().finishSpan();
    }

    @Override
    public void restoreSnapshot(String s) throws IOException, RestoreSnapshotException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.restoreSnapshot(s);
        brave.localTracer().finishSpan();
    }

    @Override
    public void restoreSnapshot(byte[] bytes, boolean b) throws IOException, RestoreSnapshotException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.restoreSnapshot(bytes, b);
        brave.localTracer().finishSpan();
    }

    @Override
    public void restoreSnapshot(String s, boolean b) throws IOException, RestoreSnapshotException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.restoreSnapshot(s, b);
        brave.localTracer().finishSpan();
    }

    @Override
    public void cloneSnapshot(byte[] bytes, TableName tableName) throws IOException, TableExistsException, RestoreSnapshotException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.cloneSnapshot(bytes, tableName);
        brave.localTracer().finishSpan();
    }

    @Override
    public void cloneSnapshot(String s, TableName tableName) throws IOException, TableExistsException, RestoreSnapshotException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.cloneSnapshot(s, tableName);
        brave.localTracer().finishSpan();
    }

    @Override
    public void execProcedure(String s, String s1, Map<String, String> map) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.execProcedure(s, s1, map);
        brave.localTracer().finishSpan();
    }

    @Override
    public byte[] execProcedureWithRet(String s, String s1, Map<String, String> map) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	byte[] res = delegate.execProcedureWithRet(s, s1, map);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean isProcedureFinished(String s, String s1, Map<String, String> map) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	boolean res = delegate.isProcedureFinished(s, s1, map);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public List<HBaseProtos.SnapshotDescription> listSnapshots() throws IOException {
        return delegate.listSnapshots();
    }

    @Override
    public List<HBaseProtos.SnapshotDescription> listSnapshots(String s) throws IOException {
        return delegate.listSnapshots(s);
    }

    @Override
    public List<HBaseProtos.SnapshotDescription> listSnapshots(Pattern pattern) throws IOException {
        return delegate.listSnapshots(pattern);
    }

    @Override
    public List<HBaseProtos.SnapshotDescription> listTableSnapshots(String s, String s1) throws IOException {
        return delegate.listTableSnapshots(s, s1);
    }

    @Override
    public List<HBaseProtos.SnapshotDescription> listTableSnapshots(Pattern pattern, Pattern pattern1) throws IOException {
        return delegate.listTableSnapshots(pattern, pattern1);
    }

    @Override
    public void deleteSnapshot(byte[] bytes) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.deleteSnapshot(bytes);
        brave.localTracer().finishSpan();
    }

    @Override
    public void deleteSnapshot(String s) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.deleteSnapshot(s);
        brave.localTracer().finishSpan();
    }

    @Override
    public void deleteSnapshots(String s) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.deleteSnapshots(s);
        brave.localTracer().finishSpan();
    }

    @Override
    public void deleteSnapshots(Pattern pattern) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.deleteSnapshots(pattern);
        brave.localTracer().finishSpan();
    }

    @Override
    public void deleteTableSnapshots(String s, String s1) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.deleteTableSnapshots(s, s1);
        brave.localTracer().finishSpan();
    }

    @Override
    public void deleteTableSnapshots(Pattern pattern, Pattern pattern1) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.deleteTableSnapshots(pattern, pattern1);
        brave.localTracer().finishSpan();
    }

    @Override
    public void setQuota(QuotaSettings quotaSettings) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.setQuota(quotaSettings);
        brave.localTracer().finishSpan();
    }

    @Override
    public QuotaRetriever getQuotaRetriever(QuotaFilter quotaFilter) throws IOException {
        return delegate.getQuotaRetriever(quotaFilter);
    }

    @Override
    public CoprocessorRpcChannel coprocessorService() {
        return delegate.coprocessorService();
    }

    @Override
    public CoprocessorRpcChannel coprocessorService(ServerName serverName) {
        return delegate.coprocessorService(serverName);
    }

    @Override
    public void updateConfiguration(ServerName serverName) throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.updateConfiguration(serverName);
        brave.localTracer().finishSpan();
    }

    @Override
    public void updateConfiguration() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	delegate.updateConfiguration();
        brave.localTracer().finishSpan();
    }

    @Override
    public int getMasterInfoPort() throws IOException {
    	brave.localTracer().startNewSpan("admin", getMethodName());
    	int res = delegate.getMasterInfoPort();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public List<SecurityCapability> getSecurityCapabilities() throws IOException {
        return delegate.getSecurityCapabilities();
    }

    @Override
    public boolean[] setSplitOrMergeEnabled(boolean b, boolean b1, MasterSwitchType... masterSwitchTypes) throws IOException {
        return delegate.setSplitOrMergeEnabled(b, b1, masterSwitchTypes);
    }

    @Override
    public boolean isSplitOrMergeEnabled(MasterSwitchType masterSwitchType) throws IOException {
        return delegate.isSplitOrMergeEnabled(masterSwitchType);
    }
}
