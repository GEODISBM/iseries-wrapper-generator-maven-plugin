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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import com.geodisbm.ws.tools.generator.engine.PgmGenerator;
import com.geodisbm.ws.tools.generator.marshaller.Marshaller;
import com.geodisbm.ws.tools.generator.marshaller.ParameterContainer;

/**
 * Goal which generates java wrapper around a PGM program based on a description file.
 * 
 * @goal generate-pgm-wrapper
 * 
 * @phase generate-sources
 */
public class PgmWrapperGeneratorMojo extends AbstractMojo {
  
  private static final String PS = System.getProperty("file.separator");

  /**
   * Location of the description file
   * @parameter expression="${basedir}/src/main/resources"
   */
  private File inputDescriptionFile;
  
  /**
   * Location of the outputDirectory
   * 
   * @parameter expression="${basedir}/src/main/java"
   * @required
   */
  private File outputDirectory;
  
  /**
   * PGM Name
   * @parameter
   * @required
   */
  private String pgmName;
  
  /**
   * Package name
   * 
   * @parameter default-value="com.geodisbm.ws.gen"
   * @required
   */
  private String packageName;
  

  public void execute() throws MojoExecutionException {
    getLog().info("*********************************");
    getLog().info("*****     Generation PGM    *****");
    getLog().info("*********************************");
    generatePgmWrapper();
  }

  private void generatePgmWrapper() {
    String absolutePath = inputDescriptionFile.getAbsolutePath();
    getLog().info("Reading input file metadatas.xml from " + absolutePath + "...");
    getLog().debug("file exists ? : " + (new File(absolutePath)).exists());
    Marshaller m = new Marshaller();
    ParameterContainer unmarshall = m.unmarshall(absolutePath);
    getLog().debug(unmarshall.toString());
    getLog().info("Reading done");
    
    PgmGenerator g = new PgmGenerator();
    g.setObject(unmarshall);
    String outputFolder = outputDirectory.getAbsolutePath() + PS + packageName.replaceAll("\\.", "\\" + PS) + PS + pgmName;
    getLog().debug("Generation root dir : " + outputFolder);
    g.setOutputFolder(outputFolder);
    getLog().info("Generating wrapper...");
    g.instantiateAll();
    getLog().info("Generating done");
    
  }

}
