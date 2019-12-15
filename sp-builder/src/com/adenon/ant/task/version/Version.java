package com.adenon.ant.task.version;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class Version extends Task {

    private String versionFile;
    private String increaseBuild;

    // The method executing the task
    @Override
    public void execute() throws BuildException {
        try {
            boolean willIncrease = true;
            if (this.increaseBuild != null) {
                if (this.increaseBuild.equalsIgnoreCase("false")) {
                    willIncrease = false;
                }
            }
            String m_Version = null;
            String m_ProjectReleaseName = null;
            int m_ProjectBuildNumber = 0;
            FileInputStream fileInputStream;

            System.out.println("Getting version info from : " + this.versionFile);

            System.out.println(System.getProperty("user.dir"));
            fileInputStream = new FileInputStream(this.versionFile);

            Properties properties = new Properties();
            properties.load(fileInputStream);

            String buildNumberStr = properties.getProperty("build");
            m_ProjectBuildNumber = Integer.parseInt(buildNumberStr);
            m_Version = properties.getProperty("version");
            m_ProjectReleaseName = properties.getProperty("releaseName");
            // write
            if (willIncrease) {
                m_ProjectBuildNumber++;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                properties.setProperty("build", Integer.toString(m_ProjectBuildNumber));
                properties.setProperty("version", m_Version);
                properties.setProperty("releaseName", m_ProjectReleaseName);
                properties.setProperty("date", dateFormat.format(new Date()));
                properties.store(new FileOutputStream(this.versionFile), m_ProjectReleaseName);
            }
            System.out.println("Version       : " + m_Version);
            System.out.println("Adenon Build  : " + m_ProjectReleaseName);
            System.out.println("Build Number  : " + m_ProjectBuildNumber);

            String versionStr = m_ProjectReleaseName + "_" + m_Version + "." + VersionChanger.getVersionPart(m_ProjectBuildNumber, 3);
            this.getProject().setProperty("release.version", versionStr);
            System.out.println("Release name : " + versionStr);
            String realReleaseTag = versionStr.replace(".", "_");
            this.getProject().setProperty("release.tag", realReleaseTag);
            System.out.println("Tag name : " + realReleaseTag);
            BuildFromXML.setVersionAndTag(m_ProjectReleaseName, versionStr, realReleaseTag);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e);
        }
    }

    // The setter for the "message" attribute
    public void setVersionFile(String _versionFile) {
        this.versionFile = _versionFile;
    }

    public void setIncreaseBuild(String _increaseBuild) {
        this.increaseBuild = _increaseBuild;
    }

    public static void main(String[] args) {
        VersionChanger changer = new VersionChanger();
        changer.setManifest("MANIFEST.MF");
        changer.execute();
    }

}
