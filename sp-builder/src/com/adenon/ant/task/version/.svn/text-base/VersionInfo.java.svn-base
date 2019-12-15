package com.adenon.ant.task.version;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class VersionInfo extends Task {
    private String manifest;

    // The method executing the task
    @Override
    public void execute() throws BuildException {
        try {
            System.out.println("manifest : " + this.manifest);
            FileInputStream fileInputStream = new FileInputStream(this.manifest);
            Manifest manife = new Manifest(fileInputStream);
            Attributes attributes = manife.getMainAttributes();
            String versionStr = attributes.getValue("Bundle-Version");
            ArrayList<Integer> returnedValues = this.getVersionParts(versionStr);
            String bundleName = attributes.getValue("Bundle-SymbolicName");
            String oVersionStr = attributes.getValue("Bundle-OVersion");
            ArrayList<Integer> oReturnedValues = null;

            if (oVersionStr != null) {
                oReturnedValues = this.getVersionParts(oVersionStr);
            }

            for (int i = returnedValues.size(); i < 4; i++) {
                returnedValues.add(0);
            }
            boolean isVersionSame = true;
            if (oReturnedValues != null) {
                for (int i = 0; i < 3; i++) {
                    if (oReturnedValues.get(i) != returnedValues.get(i)) {
                        isVersionSame = false;
                    }
                }
            }
            int buidNum = 0;

            if (oReturnedValues != null) {
                if (isVersionSame) {
                    buidNum = oReturnedValues.get(3).intValue();

                } else {
                    buidNum = 0;
                }
            } else {
                buidNum = returnedValues.get(3).intValue();
            }

            versionStr = "";
            int count = 0;
            do {
                versionStr += VersionChanger.getVersionPart(returnedValues.get(count), 2);
                versionStr += ".";
                count++;
            } while (count < 3);
            versionStr += VersionChanger.getVersionPart(buidNum, 3);
            fileInputStream.close();
            this.getProject().setProperty("version", versionStr);
            BuildFromXML.setVersionAndTag(bundleName, versionStr, "");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e);
        }
    }

    // The setter for the "message" attribute
    public void setManifest(String manifest) {
        this.manifest = manifest;
    }

    public ArrayList<Integer> getVersionParts(String versionStr) {
        StringTokenizer stringTokenizer = new StringTokenizer(versionStr, ".");
        int partCount = stringTokenizer.countTokens();
        if (partCount < 1) {
            return null;
        }
        ArrayList<Integer> retVal = new ArrayList<Integer>();
        try {
            while (stringTokenizer.hasMoreTokens()) {
                retVal.add(Integer.parseInt(stringTokenizer.nextToken()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return retVal;
    }
}