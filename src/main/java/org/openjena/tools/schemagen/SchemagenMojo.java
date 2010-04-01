/*****************************************************************************
 * File:    SchemagenMojo.java
 * Project: schemagen
 * Created: 22 Mar 2010
 * By:      ian
 *
 * Copyright (c) 2010 Epimorphics Ltd. All rights reserved.
 *****************************************************************************/

// Package
///////////////

package org.openjena.tools.schemagen;


// Imports
///////////////

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;


/**
 * <p>Maven plugin to execute Jena schemagen as part of a Jena-based
 * project build cycle
 * </p>
 *
 * @author Ian Dickinson, Epimorphics (mailto:ian@epimorphics.com)
 *
 * Maven Mojo options
 * @goal translate
 * @phase generate-sources
*/
public class SchemagenMojo
    extends AbstractMojo
{
    /***********************************/
    /* Constants                       */
    /***********************************/

    /** Default pattern for includes */

    /***********************************/
    /* Static variables                */
    /***********************************/

    /***********************************/
    /* Instance variables              */
    /***********************************/

    /**
     * Array of file patterns to include in processing
     * @parameter alias="includes"
     */
    private String[] includes = new String[0];

    /**
     * Array of file patterns to exclude from processing
     * @parameter alias="excludes"
     */
    private String[] excludes = new String[0];

    /**
     * Options for individual files
     * @parameter alias="props"
     */
    private List<Source> props;

    /**
     * The current base directory of the project
     * @parameter default-value="${basedir}"
     */
    private File baseDir;

    /***********************************/
    /* Constructors                    */
    /***********************************/

    /***********************************/
    /* External signature methods      */
    /***********************************/

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info( "includes = " + (includes == null ? null : includes[0]) );

        for (Source p: props) {
            getLog().info( "source: fileName=" + p.getFileName() );
        }
    }

    /**
     * Return a list of the file names to be processed by schemagen. These are
     * determined by processing the Ant style paths given in the <code>includes</code>
     * and <code>excludes</code> parameters.
     *
     * @return Non-null but possibly empty list of files to process
     */
    protected List<String> matchFileNames() {
        DirectoryScanner ds = new DirectoryScanner();
        ds.setExcludes( excludes );
        ds.setIncludes( includes );
        ds.setBasedir( getBaseDir() );
        ds.scan();

        return Arrays.asList( ds.getIncludedFiles() );
    }


    /***********************************/
    /* Internal implementation methods */
    /***********************************/

    public void setExcludes( String[] excludes ) {
        this.excludes = excludes;
    }

    public void setIncludes( String[] includes ) {
        this.includes = includes;
    }

    /**
     * Append the given string to the array of included file patterns
     * @param includes File pattern string to append to <code>this.includes</code>
     */
    public void addIncludes( String includes ) {
        String[] incl = new String[this.includes.length + 1];
        int i = 0;
        for (String s: this.includes) {
            incl[i++] = s;
        }
        incl[i] = includes;
        this.includes = incl;
    }

    /**
     * Append the given string to the array of excluded file patterns
     * @param excludes File pattern string to append to <code>this.excludes</code>
     */
    public void addExcludes( String excludes ) {
        String[] excl = new String[this.excludes.length + 1];
        int i = 0;
        for (String s: this.excludes) {
            excl[i++] = s;
        }
        excl[i] = excludes;
        this.excludes = excl;
    }

    /**
     * Return the base directory for the plugin, which should be supplied
     * by plexus, but if not we default to the current working directory.
     *
     * @return The base directory as a file
     */
    protected File getBaseDir() {
        return (baseDir == null) ? new File(".").getAbsoluteFile() : baseDir;
    }

    /***********************************/
    /* Inner class definitions         */
    /***********************************/

}
