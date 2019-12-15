package com.adenon.ant.task.version;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class VersionChanger extends Task {

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
            String releaseTag = attributes.getValue("Bundle-SymbolicName");
            String bundleName = attributes.getValue("Bundle-SymbolicName");
            ArrayList<Integer> returnedValues = this.getVersionParts(versionStr);

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
            buidNum++;

            versionStr = "";
            int count = 0;
            do {
                versionStr += getVersionPart(returnedValues.get(count), 2);
                versionStr += ".";
                count++;
            } while (count < 3);
            versionStr += getVersionPart(buidNum, 3);
            attributes.putValue("Bundle-OVersion", versionStr);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            attributes.putValue("Bundle-BuildTime", dateFormat.format(new Date()));
            fileInputStream.close();
            FileOutputStream fileOutputStream = new FileOutputStream(this.manifest);
            manife.write(fileOutputStream);
            this.getProject().setProperty("version", versionStr);
            if (releaseTag != null) {
                releaseTag += "." + versionStr;
                String realReleaseTag = releaseTag.replace(".", "_");
                System.out.println("Release tag for : " + releaseTag + " tag : " + realReleaseTag);
                this.getProject().setProperty("release.tag", realReleaseTag);
                BuildFromXML.setVersionAndTag(bundleName, versionStr, realReleaseTag);
            }
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

    public static String getVersionPart(int value,
                                        int retDigit) {
        String val = "" + value;
        if (val.length() >= retDigit) {
            return val;
        }
        int addcount = retDigit - val.length();
        String retVal = "";
        for (int i = 0; i < addcount; i++) {
            retVal += "0";
        }
        retVal += val;
        return retVal;
    }

    public static void main(String[] args) {
        VersionChanger changer = new VersionChanger();
        changer.setManifest("MANIFEST.MF");
        changer.execute();
    }
}
