package com.adenon.ant.task.version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Ant;

public class BuildFromFile extends Task {
    private String fileName;

    @Override
    public void execute() throws BuildException {
        try {
            System.err.println(this.fileName);
            BufferedReader input = new BufferedReader(new FileReader(this.fileName));
            try {
                String line = null;
                // Control that all of modules were checked out
                while ((line = input.readLine()) != null) {
                    if (line.contains("######")) {
                        continue;
                    }
                    File fileDir = new File("../" + line);
                    if (!fileDir.isDirectory()) {
                        System.out.println("" + line + " is not a directory!");
                        System.out.println("Please check out all bundles, services, SBBs etc.");
                        return;
                    }
                }
                input.close();
                input = new BufferedReader(new FileReader(this.fileName));

                // Build Bundles
                while (((line = input.readLine()) != null)) {
                    while (line != null) {
                        if (line.contains("## BUNDLE") || line.contains("## ENABLER")) {
                            while (((line = input.readLine()) != null) && !line.contains("####")) {
                                Ant ant = new Ant();
                                System.out.println("Building : " + line);
                                File mFile = new File("../" + line);

                                ant.setAntfile("build_bundle.xml");
                                ant.setInheritAll(true);
                                ant.setTarget("debug-bundle");
                                ant.setProject(this.getProject());
                                ant.setDir(mFile);
                                ant.execute();
                            }
                        }
                        // Build all modules except bundles
                        if (line.contains("## SBB")
                            || line.contains("## FLOW")
                            || line.contains("## RULE")
                            || line.contains("## CDR")
                            || line.contains("## SERVICE")
                            || line.contains("## WATCHDOG")) {
                            while (((line = input.readLine()) != null) && !line.contains("####")) {
                                Ant ant = new Ant();
                                System.out.println("Building : " + line);
                                File mFile = new File("../" + line + "/resources/build");

                                ant.setAntfile("build.xml");
                                ant.setInheritAll(true);
                                ant.setTarget("debug");
                                ant.setProject(this.getProject());
                                ant.setDir(mFile);
                                ant.execute();
                            }
                        }
                    }
                }
            } finally {
                input.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e);
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
