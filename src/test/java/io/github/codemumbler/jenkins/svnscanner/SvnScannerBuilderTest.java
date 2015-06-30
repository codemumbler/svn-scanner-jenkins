package io.github.codemumbler.jenkins.svnscanner;

import hudson.model.AbstractProject;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SVNRepositoryFactory.class, SVNWCUtil.class})
public class SvnScannerBuilderTest {

  public static final String SVN_URL = "svn://url/";

  @Before
  public void setUp() throws Exception {
    MockSVNRepository repository = new MockSVNRepository(SVNURL.parseURIEncoded(SVN_URL), null);
    Calendar date = Calendar.getInstance();
    repository.addSVNDirEntry(createDirEntry("/branches", "branchA", date.getTime()));
    repository.addSVNDirEntry(createDirEntry("/tags", "tagA", date.getTime()));
    date.roll(Calendar.YEAR, -1);
    repository.addSVNDirEntry(createDirEntry("/branches", "branchB", date.getTime()));
    repository.addSVNDirEntry(createDirEntry("/tags", "tagB", date.getTime()));
    PowerMockito.mockStatic(SVNRepositoryFactory.class);
    PowerMockito.doReturn(repository).when(SVNRepositoryFactory.class, "create", org.mockito.Matchers.any(SVNURL.class));
  }

  @Test
  public void findsNoOldDirectory() throws IOException, ExecutionException, InterruptedException {
    SvnScannerBuilder builder = new SvnScannerBuilder(SVN_URL, "jenkins", "1");
    //builder.perform()
  }

  private SVNDirEntry createDirEntry(String path, String name, Date date) throws SVNException {
    return new SVNDirEntry(SVNURL.parseURIEncoded(SVN_URL + path),
        SVNURL.parseURIEncoded(SVN_URL), name, SVNNodeKind.DIR, 4, false, 5, date, "user1");
  }
}
