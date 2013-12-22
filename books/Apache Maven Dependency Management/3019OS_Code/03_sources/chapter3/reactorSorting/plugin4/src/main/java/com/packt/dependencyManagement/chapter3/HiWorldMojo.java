package com.packt.dependencyManagement.chapter3;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Prints "hiWorld"
 *
 * @goal hiworld
 */
public class HiWorldMojo extends AbstractMojo {
    public void execute() throws MojoExecutionException {
        getLog().info("hiWorld");
    }
}