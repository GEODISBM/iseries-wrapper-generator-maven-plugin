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

import com.geodisbm.ws.tools.generator.engine.PgmGenerator;
import com.geodisbm.ws.tools.generator.marshaller.Marshaller;
import com.geodisbm.ws.tools.generator.marshaller.PGM;

/**
 * Goal which generates java wrapper around a PGM program based on a description file.
 * 
 * @goal generate-pgm-wrapper
 * @phase generate-sources
 */
public class PgmWrapperGeneratorMojo extends WrapperGeneratorMojo {
  

  @Override
  protected void processInputFile(File f) {
    String absolutePath = f.getParent();
    getLog().info("Reading input file metadatas.xml from " + absolutePath + "...");
    getLog().debug("file exists ? : " + (new File(absolutePath)).exists());
    
    Marshaller m = new Marshaller();
    PGM pgm = m.unmarshallPGM(absolutePath, f.getName());
    getLog().debug(pgm.toString());
    getLog().info("Reading done");
    
    PgmGenerator g = new PgmGenerator();
    g.setOutputPackage(packageName);
    g.setObject(pgm);
    String outputFolder = outputDirectory.getAbsolutePath() + PS + packageName.replaceAll("\\.", "\\" + PS) + PS + pgm.getName();
    getLog().debug("Generation root dir : " + outputFolder);
    g.setOutputFolder(outputFolder);
    getLog().info("Generating wrapper...");
    g.instantiateAll();
    getLog().info("Generating done");
    
  }

}
