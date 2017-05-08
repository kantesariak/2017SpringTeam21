package com.teambronto.svc;

import com.github.kristofa.brave.Brave;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
//import org.apache.htrace.core.SpanId;
//import org.apache.htrace.core.Tracer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * TracedConnection enables tracing on all methods part of the Connection interface. We do this through
 * a delegate wrapper construct. The surrounds all of the methods in Connection so that it is behind the
 * scenes from your average programmer. This means that interacting with TracedConnection would be identical
 * to interacting with Connection, but Zipkin tracing is performed on every method.
 */
public class TracedConnection implements Connection {
    /** The delegate that will perform the behavior */
    private Connection delegate;
    /** An instance of Brave to use for tracing */
    private Brave brave;

    public TracedConnection(@NonNull Connection delegate, @NonNull Brave brave) {
        this.delegate = delegate;
        this.brave = brave;
    }

    @Override
    public Configuration getConfiguration() {
        return delegate.getConfiguration();
    }

    @Override
    public Table getTable(TableName tableName) throws IOException {
        return new TracedTable(delegate.getTable(tableName), brave);
    }

    @Override
    public Table getTable(TableName tableName, ExecutorService executorService) throws IOException {
        return new TracedTable(delegate.getTable(tableName, executorService), brave);
    }

    @Override
    public BufferedMutator getBufferedMutator(TableName tableName) throws IOException {
        return delegate.getBufferedMutator(tableName);
    }

    @Override
    public BufferedMutator getBufferedMutator(BufferedMutatorParams bufferedMutatorParams) throws IOException {
        return delegate.getBufferedMutator(bufferedMutatorParams);
    }

    @Override
    public RegionLocator getRegionLocator(TableName tableName) throws IOException {
        return delegate.getRegionLocator(tableName);
    }

    @Override
    public Admin getAdmin() throws IOException {
        //TODO: return new TracedAdmin(delegate.getAdmin());
        return delegate.getAdmin();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    @Override
    public boolean isClosed() {
        return delegate.isClosed();
    }

    @Override
    public void abort(String s, Throwable throwable) {
        delegate.abort(s, throwable);
    }

    @Override
    public boolean isAborted() {
        return delegate.isAborted();
    }
}
