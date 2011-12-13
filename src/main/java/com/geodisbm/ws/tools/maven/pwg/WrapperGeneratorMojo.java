package com.geodisbm.ws.tools.maven.pwg;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.FileUtils;

public abstract class WrapperGeneratorMojo extends AbstractMojo {

  protected static final String PS = System.getProperty("file.separator");

  /**
   * Location of the description file
   * 
   * @parameter expression="${wrapper-generator.sourceFiles}"
   */
  protected FileSet sourceFiles;
  
  /**
   * Location of the outputDirectory
   * 
   * @parameter expression="${wrapper-generator.outputDirectory}"
   *            default-value="${basedir}/src/main/java"
   * @required
   */
  protected File outputDirectory;
  
  /**
   * Package name
   * 
   * @parameter expression="${wrapper-generator.packageName}"
   *            default-value="com.geodisbm.ws.gen"
   * @required
   */
  protected String packageName;

  protected String getCommaSeparatedList(final List<String> list) {
    final StringBuffer buffer = new StringBuffer();
    final Iterator<String> it = list.iterator();
    while (it.hasNext()) {
      Object object = it.next();
      buffer.append(object.toString());
      if (it.hasNext()) {
        buffer.append(",");
      }
    }
    return buffer.toString();
  }

  public void execute() throws MojoExecutionException {
    getLog().info("****************************************");
    getLog().info("*****     Generation SP Wrapper    *****");
    getLog().info("****************************************");
    generateWrapper();
  }

  @SuppressWarnings("unchecked")
  private void generateWrapper() throws MojoExecutionException {
    File baseDir = null;
    try {
      baseDir = new File(this.sourceFiles.getDirectory());
    }
    catch (Exception e) {
      throw new MojoExecutionException(this.sourceFiles.getDirectory() + " is not a valid path");
    }
    if (baseDir != null) {
      List<File> files = null;
      try {
        files = FileUtils.getFiles(baseDir, getCommaSeparatedList(this.sourceFiles.getIncludes()),
            getCommaSeparatedList(this.sourceFiles.getExcludes()));
        getLog().debug("Files to process : " + files.toString());
      }
      catch (IOException e) {
        getLog().warn("No source file to process");
      }
      for (final File f : files) {
        getLog().info("Processing " + f);
        processInputFile(f);
      }
    }
  }

  protected abstract void processInputFile(File f);

}