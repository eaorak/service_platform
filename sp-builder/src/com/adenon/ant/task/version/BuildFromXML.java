package com.adenon.ant.task.version;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Ant;

import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

public class BuildFromXML extends Task {

    public static final int BUILD_TYPE_DEBUG     = 0;
    public static final int BUILD_TYPE_RELEASE   = 1;

    public static final int STATUS_START         = 0;
    public static final int STATUS_UPDATE_OK     = 1;
    public static final int STATUS_UPDATE_FAILED = 2;
    public static final int STATUS_BUILD_OK      = 3;
    public static final int STATUS_BUILD_FAILED  = 4;
    public static final int STATUS_COMMIT_OK     = 5;
    public static final int STATUS_COMMIT_FAILED = 6;
    public static final int STATUS_FINSHED       = 7;

    private static class Project {
        public String name;
        public String dir;
        public String type;
        public int    status;
        public String version = "";
        public String tag     = "";
    }

    private static ArrayList<Project> projectList;

    private String                    fileName;
    private String                    resultFileName;
    private String                    baseDir;
    private String                    workspaceDir;
    private String                    releaseTask;
    private int                       buildType = BUILD_TYPE_DEBUG;

    public void setBuildType(String _buildType) {
        if ("release".equalsIgnoreCase(_buildType)) {
            this.buildType = BUILD_TYPE_RELEASE;
        } else {
            this.buildType = BUILD_TYPE_DEBUG;
        }
    }

    public void setReleaseTask(String releaseTask) {
        this.releaseTask = releaseTask;
    }

    public void setWorkspaceDir(String workspaceDir) {
        this.workspaceDir = workspaceDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public void setResultFileName(String resultFileName) {
        this.resultFileName = resultFileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void execute() throws BuildException {
        projectList = new ArrayList<Project>(20);
        System.out.println("fileName : " + this.fileName);
        int i = 0;
        try {
            if (this.buildType == BUILD_TYPE_RELEASE) {
                if ((this.resultFileName != null) && !this.resultFileName.equalsIgnoreCase("")) {
                    System.out.println("Result file : " + this.resultFileName + " baseDir:" + this.baseDir + " fileName:" + this.fileName);
                    if (!this.parseResultXML(this.resultFileName)) {
                        this.parseXML(this.baseDir + "/" + this.fileName);
                    }
                } else {
                    this.parseXML(this.baseDir + "/" + this.fileName);
                }
                for (i = 0; i < projectList.size(); i++) {
                    Project project = projectList.get(i);
                    if (project.status == STATUS_FINSHED) {
                        System.out.println("Skipping project : " + project.name);
                        continue;
                    }

                    System.out.println("Processing project name : " + project.name + " dir : " + project.dir + " type : " + project.type);
                    if ("bundle".equals(project.type)) {
                        Ant ant = this.prepareAnt(project);
                        ant.setTarget("init-bundle");
                        ant.execute();
                        this.process(project);
                    } else {
                        Ant ant = this.prepareAnt(project);
                        ant.setTarget("init");
                        ant.execute();
                        this.processPackage(project);
                    }
                }
            } else {
                if ((this.resultFileName != null) && !this.resultFileName.equalsIgnoreCase("")) {
                    System.out.println("Result file : " + this.resultFileName);
                    if (!this.parseResultXML(this.resultFileName)) {
                        this.parseXML(this.baseDir + "/" + this.fileName);
                    }
                } else {
                    this.parseXML(this.baseDir + "/" + this.fileName);
                }
                for (i = 0; i < projectList.size(); i++) {
                    Project project = projectList.get(i);
                    System.out.println("Processing project name : " + project.name + " dir : " + project.dir + " type : " + project.type);
                    if ("bundle".equals(project.type)) {
                        Ant ant = this.prepareAnt(project);
                        ant.setTarget("init-bundle");
                        ant.execute();
                        this.processDebug(project);
                    } else {
                        Ant ant = this.prepareAnt(project);
                        ant.setTarget("init");
                        ant.execute();
                        this.processPackageDebug(project);
                    }
                }
            }
        } catch (Exception e) {
            try {
                this.generateResultXML(this.resultFileName);
            } catch (Exception e2) {
            }
            System.out.println("Error : " + e.getMessage());
            throw new BuildException(e);
        }
        try {
            this.generateResultXML(this.resultFileName);
        } catch (Exception e2) {
        }
        try {
            this.generateResultXML(this.resultFileName);
        } catch (Exception e2) {
        }
    }

    public Ant prepareAnt(Project project) {
        Ant ant = new Ant();
        if ("bundle".equals(project.type)) {
            File fileDir = new File(this.workspaceDir + "/" + project.dir);
            ant.setAntfile("build_bundle.xml");
            ant.setInheritAll(true);
            ant.setProject(this.getProject());
            ant.setDir(fileDir);
            this.getProject().setBaseDir(fileDir);
        } else {
            File fileDir = new File(this.workspaceDir + "/" + project.dir + "/resources/build");
            ant.setAntfile("build.xml");
            ant.setInheritAll(true);
            ant.setProject(this.getProject());
            ant.setDir(fileDir);
            this.getProject().setBaseDir(fileDir);
        }
        return ant;
    }

    public void process(Project project) throws Exception {
        switch (project.status) {
            case STATUS_START:
            case STATUS_UPDATE_FAILED:
                try {
                    System.out.println("********** Updating : " + project.name);
                    Ant antCloneForUpdate = this.prepareAnt(project);
                    antCloneForUpdate.getProject().setProperty("release.task", this.releaseTask);
                    antCloneForUpdate.setTarget("init-bundle");
                    antCloneForUpdate.setTarget("update");
                    antCloneForUpdate.execute();
                    project.status = STATUS_UPDATE_OK;
                } catch (Exception e) {
                    project.status = STATUS_UPDATE_FAILED;
                    System.err.println("Error while updating project name : " + project.name + " dir : " + project.dir);
                    throw e;
                }
            case STATUS_UPDATE_OK:
            case STATUS_BUILD_FAILED:
                try {
                    System.out.println("********** Building : " + project.name);
                    String str = this.getRepositoryName(project.type);
                    System.out.println("Str : " + str);
                    String destination = this.getProject().getProperty(str);
                    System.out.println("Destination : " + destination);
                    Ant antCloneForBuild = this.prepareAnt(project);
                    antCloneForBuild.getProject().setProperty("destination", destination);
                    antCloneForBuild.setTarget("init-bundle");
                    antCloneForBuild.setTarget("build-only");
                    antCloneForBuild.execute();
                    project.status = STATUS_BUILD_OK;
                } catch (Exception e) {
                    project.status = STATUS_BUILD_FAILED;
                    System.err.println("Error while building project name : " + project.name + " dir : " + project.dir);
                    throw e;
                }
            case STATUS_BUILD_OK:
            case STATUS_COMMIT_FAILED:
                try {
                    System.out.println("********** Commiting : " + project.name);
                    Ant antCloneForCommit = this.prepareAnt(project);
                    antCloneForCommit.getProject().setProperty("releaseName", project.name + "_" + project.version);
                    antCloneForCommit.getProject().setProperty("release.tag", project.tag);
                    antCloneForCommit.getProject().setProperty("release.task", this.releaseTask);
                    antCloneForCommit.setTarget("init-bundle");
                    antCloneForCommit.setTarget("commit");
                    antCloneForCommit.execute();
                    project.status = STATUS_COMMIT_OK;
                } catch (Exception e) {
                    project.status = STATUS_COMMIT_FAILED;
                    System.err.println("Error while commiting project name : " + project.name + " dir : " + project.dir);
                    throw e;
                }
            case STATUS_FINSHED:
                project.status = STATUS_FINSHED;
                System.out.println("Project processed : " + project.name);
                break;
            default:
                System.err.println("Wrong status " + project.status);
                break;
        }
    }

    public void processDebug(Project project) throws Exception {
        switch (project.status) {
            case STATUS_START:
            case STATUS_UPDATE_FAILED:
            case STATUS_UPDATE_OK:
            case STATUS_BUILD_FAILED:
            case STATUS_BUILD_OK:
            case STATUS_COMMIT_FAILED:
            case STATUS_COMMIT_OK:
                try {
                    System.out.println("********** Building : " + project.name);
                    String str = this.getRepositoryName(project.type);
                    System.out.println("Str : " + str);
                    String destination = this.getProject().getProperty(str);
                    System.out.println("Destination : " + destination);
                    Ant antCloneForBuild = this.prepareAnt(project);
                    antCloneForBuild.getProject().setProperty("destination", destination);
                    antCloneForBuild.getProject().setProperty("release.file", "ReleaseNotes.txt");
                    antCloneForBuild.setTarget("init-bundle");
                    antCloneForBuild.setTarget("build-only-debug");
                    antCloneForBuild.execute();
                    project.status = STATUS_BUILD_OK;
                } catch (Exception e) {
                    project.status = STATUS_BUILD_FAILED;
                    System.err.println("Error while building project name : " + project.name + " dir : " + project.dir);
                    throw e;
                }
            case STATUS_FINSHED:
                project.status = STATUS_FINSHED;
                System.out.println("Project processed : " + project.name);
                break;
            default:
                System.err.println("Wrong status " + project.status);
                break;
        }
    }

    public void processPackageDebug(Project project) throws Exception {
        switch (project.status) {
            case STATUS_START:
            case STATUS_UPDATE_FAILED:
            case STATUS_UPDATE_OK:
            case STATUS_BUILD_FAILED:
            case STATUS_BUILD_OK:
            case STATUS_COMMIT_FAILED:
            case STATUS_COMMIT_OK:
                try {
                    System.out.println("********** Building : " + project.name);
                    String str = this.getRepositoryName(project.type);
                    System.out.println("Str : " + str);
                    System.out.println(" >>> PROPERTY :: " + str);
                    String destination = this.getProject().getProperty(str);
                    System.out.println("Destination : " + destination);
                    Ant antCloneForBuild = this.prepareAnt(project);
                    antCloneForBuild.getProject().setProperty("deploy.dir", destination);
                    antCloneForBuild.getProject().setProperty("release.file", "ReleaseNotes.txt");
                    antCloneForBuild.setTarget("init");
                    antCloneForBuild.setTarget("build-only-debug");
                    antCloneForBuild.execute();
                    project.status = STATUS_BUILD_OK;
                } catch (Exception e) {
                    project.status = STATUS_BUILD_FAILED;
                    System.err.println("Error while building project name : " + project.name + " dir : " + project.dir);
                    throw e;
                }
            case STATUS_FINSHED:
                project.status = STATUS_FINSHED;
                System.out.println("Project processed : " + project.name);
                break;
            default:
                System.err.println("Wrong status " + project.status);
                break;
        }
    }

    public void processPackage(Project project) throws Exception {
        switch (project.status) {
            case STATUS_START:
            case STATUS_UPDATE_FAILED:
                try {
                    System.out.println("Updating : " + project.name);
                    Ant antCloneForUpdate = this.prepareAnt(project);
                    antCloneForUpdate.getProject().setProperty("release.task", this.releaseTask);
                    antCloneForUpdate.setTarget("init");
                    antCloneForUpdate.setTarget("update");
                    antCloneForUpdate.execute();
                    project.status = STATUS_UPDATE_OK;
                } catch (Exception e) {
                    project.status = STATUS_UPDATE_FAILED;
                    System.err.println("Error while updating project name : " + project.name + " dir : " + project.dir);
                    throw e;
                }
            case STATUS_UPDATE_OK:
            case STATUS_BUILD_FAILED:
                try {
                    System.out.println("Building : " + project.name);
                    String str = this.getRepositoryName(project.type);
                    System.out.println(" >>> PROPERTY :: " + str);
                    String destination = this.getProject().getProperty(str);
                    System.out.println("Destination : " + destination);
                    Ant antCloneForBuild = this.prepareAnt(project);
                    antCloneForBuild.getProject().setProperty("deploy.dir", destination);
                    antCloneForBuild.setTarget("init");
                    antCloneForBuild.setTarget("build-only");
                    antCloneForBuild.execute();
                    project.status = STATUS_BUILD_OK;
                } catch (Exception e) {
                    project.status = STATUS_BUILD_FAILED;
                    System.err.println("Error while building project name : " + project.name + " dir : " + project.dir);
                    throw e;
                }
            case STATUS_BUILD_OK:
            case STATUS_COMMIT_FAILED:
                try {
                    System.out.println("Commiting : " + project.name);
                    Ant antCloneForCommit = this.prepareAnt(project);
                    System.out.println("Release Name : " + project.version);
                    antCloneForCommit.getProject().setProperty("release.version", project.version);
                    antCloneForCommit.getProject().setProperty("release.tag", project.tag);
                    antCloneForCommit.getProject().setProperty("release.task", this.releaseTask);
                    antCloneForCommit.getProject().setProperty("releaseName", project.name + "_" + project.version);
                    antCloneForCommit.setTarget("init");
                    antCloneForCommit.setTarget("commit");
                    antCloneForCommit.execute();
                    project.status = STATUS_COMMIT_OK;
                } catch (Exception e) {
                    project.status = STATUS_COMMIT_FAILED;
                    System.err.println("Error while commiting project name : " + project.name + " dir : " + project.dir);
                    throw e;
                }
            case STATUS_FINSHED:
                project.status = STATUS_FINSHED;
                System.out.println("Project processed : " + project.name);
                break;
            default:
                System.err.println("Wrong status " + project.status);
                break;
        }
    }

    public void parseXML(String fileName) throws Exception {
        System.out.println("Parsing file : " + fileName);
        VTDGen vg = new VTDGen();
        if (vg.parseFile(fileName, true)) {
            VTDNav vn = vg.getNav();
            if (vn.matchElement("projects")) {
                if (vn.toElement(VTDNav.FIRST_CHILD)) {
                    do {
                        if (vn.matchElement("project")) {
                            Project project = new Project();
                            project.name = this.getAttribute(vn, "name");
                            project.dir = this.getAttribute(vn, "dir");
                            project.type = this.getAttribute(vn, "type");
                            BuildFromXML.projectList.add(project);
                            System.out.println("Project parsed name : " + project.name + " dir : " + project.dir + " type : " + project.type);
                        }
                    } while (vn.toElement(VTDNav.NEXT_SIBLING));
                }
            }
        } else {
            System.err.println("Error : didnt find the file : " + fileName);
        }
    }

    public boolean parseResultXML(String fileName) throws Exception {
        VTDGen vg = new VTDGen();
        if (vg.parseFile(fileName, true)) {
            VTDNav vn = vg.getNav();
            if (vn.matchElement("projects")) {
                if (vn.toElement(VTDNav.FIRST_CHILD)) {
                    do {
                        if (vn.matchElement("project")) {
                            Project project = new Project();
                            project.name = this.getAttribute(vn, "name");
                            project.dir = this.getAttribute(vn, "dir");
                            project.type = this.getAttribute(vn, "type");
                            project.version = this.getAttribute(vn, "version");
                            project.tag = this.getAttribute(vn, "tag");
                            project.status = Integer.parseInt(this.getAttribute(vn, "status"));
                            BuildFromXML.projectList.add(project);
                        }
                    } while (vn.toElement(VTDNav.NEXT_SIBLING));
                }
            }
            return true;
        }
        return false;

    }

    public String getValue(VTDNav vn) throws Exception {
        int j = vn.getText();
        if (j != -1) {
            return vn.toString(j).trim();
        }
        return null;
    }

    public String getAttribute(VTDNav vn, String attributeName) throws Exception {
        int j = vn.getAttrVal(attributeName);
        if (j != -1) {
            return vn.toString(j).trim();
        }
        return null;
    }

    public void generateResultXML(String fileName) throws Exception {
        if (fileName == null) {
            return;
        }
        if ("".equals(fileName)) {
            return;
        }
        FileWriter fstream = new FileWriter(fileName);
        BufferedWriter out = new BufferedWriter(fstream);

        // Close the output stream
        out.write("<projects>\r\n");
        for (int i = 0; i < projectList.size(); i++) {
            Project project = projectList.get(i);
            out.write("  <project name=\""
                      + project.name
                      + "\" dir=\""
                      + project.dir
                      + "\" type=\""
                      + project.type
                      + "\" version=\""
                      + project.version
                      + "\" tag=\""
                      + project.tag
                      + "\" status=\""
                      + project.status
                      + "\"/>\r\n");
        }
        out.write("</projects>");
        out.close();
    }

    public static void main(String[] args) {
        BuildFromXML buildFromXML = new BuildFromXML();
        buildFromXML.setFileName("corebuild.xml");
        buildFromXML.execute();
    }

    public static void setVersionAndTag(String name, String version, String tag) {
        if (projectList == null) {
            return;
        }
        for (int i = 0; i < projectList.size(); i++) {
            Project project = projectList.get(i);
            if (project.name.equals(name)) {
                project.version = version;
                project.tag = tag;
            }
        }
    }

    public String getRepositoryName(String type) {
        if ("bundle".equalsIgnoreCase(type)) {
            return "dir.osgi.bundles";
        }
        return "unknown";
    }
}
