package io.github.codemumbler.jenkins.svnscanner;

import hudson.remoting.Callable;
import hudson.remoting.Future;
import hudson.remoting.VirtualChannel;

import java.io.IOException;

public class MockVirtualChannel implements VirtualChannel {
  @Override
  public <V, T extends Throwable> V call(Callable<V, T> callable) throws IOException, T, InterruptedException {
    return null;
  }

  @Override
  public <V, T extends Throwable> Future<V> callAsync(Callable<V, T> callable) throws IOException {
    return null;
  }

  @Override
  public void close() throws IOException {

  }

  @Override
  public void join() throws InterruptedException {

  }

  @Override
  public void join(long l) throws InterruptedException {

  }

  @Override
  public <T> T export(Class<T> aClass, T t) {
    return null;
  }

  @Override
  public void syncLocalIO() throws InterruptedException {

  }
}
