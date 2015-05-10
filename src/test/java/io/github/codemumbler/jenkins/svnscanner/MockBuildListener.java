package io.github.codemumbler.jenkins.svnscanner;

import hudson.console.ConsoleNote;
import hudson.model.BuildListener;
import hudson.model.Cause;
import hudson.model.Result;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

public class MockBuildListener implements BuildListener {
	@Override
	public void started(List<Cause> list) {

	}

	@Override
	public void finished(Result result) {

	}

	@Override
	public PrintStream getLogger() {
		return null;
	}

	@Override
	public void annotate(ConsoleNote consoleNote) throws IOException {

	}

	@Override
	public void hyperlink(String s, String s1) throws IOException {

	}

	@Override
	public PrintWriter error(String s) {
		return null;
	}

	@Override
	public PrintWriter error(String s, Object... objects) {
		return null;
	}

	@Override
	public PrintWriter fatalError(String s) {
		return null;
	}

	@Override
	public PrintWriter fatalError(String s, Object... objects) {
		return null;
	}
}
