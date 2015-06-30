package io.github.codemumbler.jenkins.svnscanner;

import hudson.FilePath;
import hudson.Launcher;
import hudson.Proc;
import hudson.model.TaskListener;
import hudson.remoting.Channel;
import hudson.remoting.VirtualChannel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class MockLauncher extends Launcher {

  public MockLauncher(TaskListener listener, VirtualChannel channel) {
    super(listener, channel);
  }

  @Override
  public Proc launch(ProcStarter procStarter) throws IOException {
    return null;
  }

  @Override
  public Channel launchChannel(String[] strings, OutputStream outputStream, FilePath filePath, Map<String, String> map) throws IOException, InterruptedException {
    return null;
  }

  @Override
  public void kill(Map<String, String> map) throws IOException, InterruptedException {

  }
}
