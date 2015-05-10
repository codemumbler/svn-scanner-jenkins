package io.github.codemumbler.jenkins.svnscanner;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import org.kohsuke.stapler.DataBoundConstructor;
import org.tmatesoft.svn.core.SVNException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class SvnScannerBuilder extends Builder {

	private String svnUrl;
	private String username;
	private String password;
	private String staleMonths;

	@DataBoundConstructor
	public SvnScannerBuilder(String svnUrl, String username, String password, String staleMonths) {
		this.svnUrl = svnUrl;
		this.username = username;
		this.password = password;
		this.staleMonths = staleMonths;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
		if (build == null) return false;
		try {
			listener.getLogger().println("Initializing SVN Scanner... ");
			SVNScanner scanner = new SVNScanner(svnUrl, username, password);
			listener.getLogger().println("SVN Scanner authenticated");
			Calendar olderThan = Calendar.getInstance();
			olderThan.add(Calendar.MONTH, (-1 * Integer.valueOf(staleMonths)));
			List<String> directories = scanner.getDirectories("/branches", olderThan);
			if ( directories.isEmpty() ) {
				return true;
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

	public String getStaleMonths() {
		return staleMonths;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Extension
	public static class Descriptor extends BuildStepDescriptor<Builder> {

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return FreeStyleProject.class.isAssignableFrom(jobType);
		}

		@Override
		public String getDisplayName() {
			return "SVN Staleness";
		}
	}
}
