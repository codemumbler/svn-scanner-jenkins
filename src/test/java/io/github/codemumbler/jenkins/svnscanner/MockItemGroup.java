package io.github.codemumbler.jenkins.svnscanner;

import hudson.model.Item;
import hudson.model.ItemGroup;
import org.acegisecurity.AccessDeniedException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class MockItemGroup implements ItemGroup {
  @Override
  public String getFullName() {
    return "test";
  }

  @Override
  public String getFullDisplayName() {
    return "test";
  }

  @Override
  public Collection getItems() {
    return null;
  }

  @Override
  public String getUrl() {
    return null;
  }

  @Override
  public String getUrlChildPrefix() {
    return null;
  }

  @Override
  public Item getItem(String s) throws AccessDeniedException {
    return null;
  }

  @Override
  public File getRootDirFor(Item item) {
    return new File("target/test-job");
  }

  @Override
  public void onRenamed(Item item, String s, String s1) throws IOException {

  }

  @Override
  public void onDeleted(Item item) throws IOException {

  }

  @Override
  public String getDisplayName() {
    return null;
  }

  @Override
  public File getRootDir() {
    return null;
  }

  @Override
  public void save() throws IOException {

  }
}
