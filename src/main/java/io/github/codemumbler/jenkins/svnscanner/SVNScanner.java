package io.github.codemumbler.jenkins.svnscanner;

import com.cloudbees.plugins.credentials.common.StandardCredentials;
import hudson.scm.CredentialsSVNAuthenticationProviderImpl;
import hudson.scm.FilterSVNAuthenticationManager;
import hudson.scm.SVNAuthStoreHandlerImpl;
import hudson.scm.SVNAuthenticationManager;
import hudson.util.TimeUnit2;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class SVNScanner {

  private static final String BRANCHES = "/branches";
  private static final String TAGS = "/tags";

  static {
    DAVRepositoryFactory.setup();
    SVNRepositoryFactoryImpl.setup();
    FSRepositoryFactory.setup();
  }

  private SVNRepository repository;

  public SVNScanner(String url, String username, String password) throws SVNException {
    repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
    ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
    repository.setAuthenticationManager(authManager);
  }

  public SVNScanner(String svnUrl, StandardCredentials credentials) throws SVNException {
    repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl));
    File configDir = SVNWCUtil.getDefaultConfigurationDirectory();
    ISVNAuthenticationManager sam = new SVNAuthenticationManager(configDir, null, null);
    sam.setAuthenticationProvider(new CredentialsSVNAuthenticationProviderImpl(credentials));
    SVNAuthStoreHandlerImpl.install(sam);
    sam = new FilterSVNAuthenticationManager(sam) {
      // If there's no time out, the blocking read operation may hang forever, because TCP itself
      // has no timeout. So always use some time out. If the underlying implementation gives us some
      // value (which may come from ~/.subversion), honor that, as long as it sets some timeout value.
      @Override
      public int getReadTimeout(SVNRepository repository) {
        int r = super.getReadTimeout(repository);
        if (r <= 0) {
          r = (int) TimeUnit2.MINUTES.toMillis(1);
        }
        return r;
      }
    };
    repository.setTunnelProvider(SVNWCUtil.createDefaultOptions(true));
    repository.setAuthenticationManager(sam);
  }

  public List<String> listBranches() throws SVNException {
    return getDirectories(BRANCHES, Calendar.getInstance());
  }

  public List<String> listBranches(Calendar olderThan) throws SVNException {
    return getDirectories(BRANCHES, olderThan);
  }

  public List<String> getDirectories(String path, Calendar olderThan) throws SVNException {
    List<String> branches = new ArrayList<>();
    List<SVNDirEntry> entries = (List<SVNDirEntry>) repository.getDir(path, -1, null, (Collection) null);
    for (SVNDirEntry entry : entries) {
      if (olderThan.getTime().after(entry.getDate()))
        branches.add(entry.getName());
    }
    return branches;
  }

  public List<String> listTags(Calendar olderThan) throws SVNException {
    return getDirectories(TAGS, olderThan);
  }
}
