package com.geodisbm.ws.tools.maven.pwg;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.FileUtils;

import com.geodisbm.ws.tools.generator.Marshaller;
import com.geodisbm.ws.tools.generator.PSGenerator;
import com.geodisbm.ws.tools.generator.StoredProcedure;

/**
 * Goal which generates java wrapper around a Stored Procedure based on a
 * description file.
 * 
 * @goal generate-procedure-wrapper
 * 
 */
public class PSWrapperGeneratorMojo extends AbstractMojo {

  private static final String PS = System.getProperty("file.separator");

  /**
   * Location of the description file
   * 
   * @parameter expression="${ps-wrapper-generator.sourceFiles}"
   */
  private FileSet sourceFiles;

  /**
   * Location of the outputDirectory
   * 
   * @parameter expression="${ps-wrapper-generator.outputDirectory}"
   *            default-value="${basedir}/src/main/java"
   * @required
   */
  private File outputDirectory;

  /**
   * Package name
   * 
   * @parameter expression="${ps-wrapper-generator.packageName}"
   *            default-value="com.geodisbm.ws.gen"
   * @required
   */
  private String packageName;

  public void execute() throws MojoExecutionException {
    getLog().info("****************************************");
    getLog().info("*****     Generation SP Wrapper    *****");
    getLog().info("****************************************");
    generateSPWrapper();
  }

  @SuppressWarnings("unchecked")
  private void generateSPWrapper() throws MojoExecutionException {
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
        processPSInputFile(f);
      }
    }
  }

  private void processPSInputFile(File f) {
    String absolutePath = f.getParent();
    getLog().info("Reading input file from " + absolutePath + "...");
    getLog().debug("file exists ? : " + (new File(absolutePath)).exists());
    Marshaller m = new Marshaller();
    StoredProcedure storedProcedure = m.unmarshallStoredProcedure(absolutePath, f.getName());
    getLog().debug(storedProcedure.toString());
    getLog().info("Reading done");
    PSGenerator g = new PSGenerator();
    g.setObject(storedProcedure);
    String outputFolder = outputDirectory.getAbsolutePath() + PS + packageName.replaceAll("\\.", "\\" + PS) + PS
        + storedProcedure.getName();
    getLog().debug("Generation root dir : " + outputFolder);
    g.setOutputFolder(outputFolder);
    getLog().info("Generating wrapper...");
    g.instantiateAll();
    getLog().info("Generating done");
  }

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

}
