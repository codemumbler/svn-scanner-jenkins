package io.github.codemumbler.jenkins.svnscanner;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.domains.URIRequirementBuilder;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.security.ACL;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.ListBoxModel;
import org.kohsuke.stapler.DataBoundConstructor;
import org.tmatesoft.svn.core.SVNException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class SvnScannerBuilder extends Builder {

  private String svnUrl;
  private String credentialsId;
  private String staleMonths;

  @DataBoundConstructor
  public SvnScannerBuilder(String svnUrl, String credentialsId, String staleMonths) {
    this.svnUrl = svnUrl;
    this.credentialsId = credentialsId;
    this.staleMonths = staleMonths;
  }

  @Override
  public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
    if (build == null) return false;
    try {
      StandardCredentials c = CredentialsMatchers.firstOrNull(CredentialsProvider.lookupCredentials(StandardCredentials.class, build.getProject(), ACL.SYSTEM, URIRequirementBuilder.fromUri(this.svnUrl).build()),
          CredentialsMatchers.withId(credentialsId));
      SVNScanner scanner = new SVNScanner(svnUrl, c);
      listener.getLogger().println("Scanning " + svnUrl + " for stale branches.");
      Calendar olderThan = Calendar.getInstance();
      olderThan.add(Calendar.MONTH, (-1 * Integer.valueOf(staleMonths)));
      List<String> directories = scanner.listBranches(olderThan);
      if (directories.isEmpty()) {
        return true;
      } else {
        for (String directory : directories) {
          listener.getLogger().println("Found the following stale branches: " + directory);
        }
      }
      listener.getLogger().println(directories.toString());
    } catch (SVNException e) {
      e.printStackTrace(listener.getLogger());
      build.setResult(Result.FAILURE);
      return false;
    }
    build.setResult(Result.UNSTABLE);
    return true;
  }

  public String getSvnUrl() {
    return svnUrl;
  }

  public String getCredentialsId() {
    return credentialsId;
  }

  public String getStaleMonths() {
    return staleMonths;
  }

  @Extension
  public static class Descriptor extends BuildStepDescriptor<Builder> {

    @Override
    public String getDisplayName() {
      return "SVN Staleness";
    }

    public ListBoxModel doFillCredentialsIdItems() {
      StandardListBoxModel model = new StandardListBoxModel();
      model.withEmptySelection().withMatching(CredentialsMatchers.always(), CredentialsProvider.lookupCredentials(StandardCredentials.class));
      return model;
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> aClass) {
      return true;
    }
  }
}
