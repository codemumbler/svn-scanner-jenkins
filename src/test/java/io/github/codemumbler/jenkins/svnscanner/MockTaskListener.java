package io.github.codemumbler.jenkins.svnscanner;

import hudson.console.ConsoleNote;
import hudson.model.TaskListener;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class MockTaskListener implements TaskListener {
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
