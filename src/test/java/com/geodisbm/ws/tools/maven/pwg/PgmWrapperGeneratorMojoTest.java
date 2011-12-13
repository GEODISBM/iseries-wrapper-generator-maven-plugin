package com.geodisbm.ws.tools.maven.pwg;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;


public class PgmWrapperGeneratorMojoTest {

  @Test
  public void testE340AS1USW() throws MojoExecutionException {
    PgmWrapperGeneratorMojo generator = new PgmWrapperGeneratorMojo();
    FileSet e340as1uswFile = new FileSet();
    e340as1uswFile.setDirectory("src/test/resources");
    e340as1uswFile.addInclude("E340AS1USW.xml");
    generator.sourceFiles = e340as1uswFile ;
    generator.packageName = "com.geodisbm.test";
    generator.outputDirectory = new File("target");
    generator.execute();
    assertTrue(new File("target/com/geodisbm/test/E340AS1USW/E340AS1USWPgmCall.java").exists());
    assertTrue(new File("target/com/geodisbm/test/E340AS1USW/E340AS1USWDataBean.java").exists());
    assertTrue(new File("target/com/geodisbm/test/E340AS1USW/E340AS1USWSTRUCTUREDataStructure.java").exists());
    assertTrue(new File("target/com/geodisbm/test/E340AS1USW/E340AS1USWPgmParametersFactory.java").exists());
  }
  
}
