package com.teambronto.svc;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.LocalTracer;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.Service;
import com.google.protobuf.ServiceException;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang.NullArgumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.ipc.CoprocessorRpcChannel;
import org.apache.hadoop.hbase.ipc.RpcControllerFactory;
//import org.apache.htrace.core.SpanId;
//import org.apache.htrace.core.TraceScope;
//import org.apache.htrace.core.Tracer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;


/**
 * TracedTable wraps all of org.apache.hbase.client.HTable methods with HTrace tracing. This way, whenever an action is
 * performed on the table (get, put, etc.) it is logged to Zipkin.
 */
public class TracedTable implements Table {
    private Table delegate;
    /**
     * Brave
     */
    private Brave brave;

    public TracedTable(@NonNull Table delegate, @NonNull Brave brave) {
        this.delegate = delegate;
        this.brave = brave;
    }
    
    /**
     * Gets the method name by looking into the stacktrace
     * Very inefficient in terms of performance, TODO: change to string literal for each function
     * @return current HBase MethodName
     */
    @Deprecated
    private String getMethodName(){
        return Thread.currentThread().getStackTrace()[2].getMethodName();

    }

    @Override
    public Configuration getConfiguration() {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        Configuration res = delegate.getConfiguration();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public HTableDescriptor getTableDescriptor() throws IOException {
        HTableDescriptor res = new HTableDescriptor();
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        res = delegate.getTableDescriptor();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public TableName getName() {
        return delegate.getName();
    }

    @Override
    public ResultScanner getScanner(Scan scan) throws IOException {
        return delegate.getScanner(scan);
    }

    @Override
    public ResultScanner getScanner(byte[] family) throws IOException {
        return delegate.getScanner(family);
    }

    @Override
    public ResultScanner getScanner(byte[] family, byte[] qualifier) throws IOException {
        return delegate.getScanner(family, qualifier);
    }

    @Override
    public Result get(Get get) throws IOException {
        brave.localTracer().startNewSpan("HBase Client", getMethodName());
        Result res = delegate.get(get);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public Result[] get(List<Get> gets) throws IOException {
        return delegate.get(gets);
    }

    @Override
    public void batch(List<? extends Row> actions, Object[] results) throws InterruptedException, IOException {
        delegate.batch(actions, results);
    }

    @Override
    public Object[] batch(List<? extends Row> list) throws IOException, InterruptedException {
        return delegate.batch(list);
    }

    @Override
    public <R> Object[] batchCallback(List<? extends Row> list, Batch.Callback<R> callback) throws IOException, InterruptedException {
        return delegate.batchCallback(list, callback);
    }

    @Override
    public <R> void batchCallback(List<? extends Row> actions, Object[] results, Batch.Callback<R> callback) throws IOException, InterruptedException {
        delegate.batchCallback(actions, results, callback);
    }

    @Override
    public void delete(Delete delete) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        delegate.delete(delete);
        brave.localTracer().finishSpan();
    }

    @Override
    public void delete(List<Delete> deletes) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        delegate.delete(deletes);
        brave.localTracer().finishSpan();

    }

    @Override
    public void put(Put put) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        delegate.put(put);
        brave.localTracer().finishSpan();
    }

    @Override
    public void put(List<Put> puts) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        delegate.put(puts);
        brave.localTracer().finishSpan();
    }

    @Override
    public void mutateRow(RowMutations rm) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        delegate.mutateRow(rm);
        brave.localTracer().finishSpan();

    }

    @Override
    public Result append(Append append) throws IOException {
        Result res = new Result();
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        res = delegate.append(append);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public Result increment(Increment increment) throws IOException {
        Result res = new Result();
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        res = delegate.increment(increment);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public long incrementColumnValue(byte[] row, byte[] family, byte[] qualifier, long amount) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        Long res = delegate.incrementColumnValue(row, family, qualifier, amount);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public long incrementColumnValue(byte[] row, byte[] family, byte[] qualifier, long amount, Durability durability) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        Long res = delegate.incrementColumnValue(row, family, qualifier, amount, durability);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean checkAndPut(byte[] row, byte[] family, byte[] qualifier, byte[] value, Put put) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        boolean res = delegate.checkAndPut(row, family, qualifier, value, put);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean checkAndPut(byte[] row, byte[] family, byte[] qualifier, CompareFilter.CompareOp compareOp, byte[] value, Put put) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        boolean res = delegate.checkAndPut(row, family, qualifier, compareOp, value, put);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean checkAndDelete(byte[] row, byte[] family, byte[] qualifier, byte[] value, Delete delete) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        boolean res = delegate.checkAndDelete(row, family, qualifier, value, delete);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean checkAndDelete(byte[] row, byte[] family, byte[] qualifier, CompareFilter.CompareOp compareOp, byte[] value, Delete delete) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        boolean res = delegate.checkAndDelete(row, family, qualifier, compareOp, value, delete);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean checkAndMutate(byte[] row, byte[] family, byte[] qualifier, CompareFilter.CompareOp compareOp, byte[] value, RowMutations rm) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        boolean res = delegate.checkAndMutate(row, family, qualifier, compareOp, value, rm);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean exists(Get get) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        boolean res = delegate.exists(get);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public boolean[] existsAll(List<Get> gets) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        boolean[] res = delegate.existsAll(gets);
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public void close() throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        delegate.close();
        brave.localTracer().finishSpan();
    }

    @Override
    public long getWriteBufferSize() {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        long res = delegate.getWriteBufferSize();
        brave.localTracer().finishSpan();
        return res;
    }

    @Override
    public void setWriteBufferSize(long writeBufferSize) throws IOException {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        delegate.setWriteBufferSize(writeBufferSize);
        brave.localTracer().finishSpan();
    }

    @Override
    public CoprocessorRpcChannel coprocessorService(byte[] row) {
        brave.localTracer().startNewSpan(getName().getNameAsString(), getMethodName());
        brave.localTracer().submitAnnotation("this span was not traced");
        brave.localTracer().finishSpan();
        return delegate.coprocessorService(row);
    }

    @Override
    public <T extends Service, R> Map<byte[], R> coprocessorService(Class<T> service, byte[] startKey, byte[] endKey, Batch.Call<T, R> callable) throws Throwable, ServiceException {
        return delegate.coprocessorService(service, startKey, endKey, callable);
    }

    @Override
    public <T extends Service, R> void coprocessorService(Class<T> service, byte[] startKey, byte[] endKey, Batch.Call<T, R> callable, Batch.Callback<R> callback) throws Throwable, ServiceException {
        delegate.coprocessorService(service, startKey, endKey, callable, callback);
    }

    @Override
    public void setOperationTimeout(int operationTimeout) {
        delegate.setOperationTimeout(operationTimeout);
    }

    @Override
    public int getOperationTimeout() {
        return delegate.getOperationTimeout();
    }

    @Override
    public void setRpcTimeout(int rpcTimeout) {
        delegate.setRpcTimeout(rpcTimeout);
    }

    @Override
    public int getRpcTimeout() {
        return delegate.getRpcTimeout();
    }

    @Override
    public <R extends Message> Map<byte[], R> batchCoprocessorService(Descriptors.MethodDescriptor methodDescriptor,
                                                                      Message request, byte[] startKey, byte[] endKey,
                                                                      R responsePrototype) throws Throwable {
        return delegate.batchCoprocessorService(methodDescriptor, request, startKey, endKey, responsePrototype);
    }

    @Override
    public <R extends Message> void batchCoprocessorService(Descriptors.MethodDescriptor methodDescriptor,
                                                            Message request, byte[] startKey, byte[] endKey,
                                                            R responsePrototype, Batch.Callback<R> callback) throws Throwable {
        delegate.batchCoprocessorService(methodDescriptor, request, startKey, endKey, responsePrototype, callback);
    }
}
