package com.adenon.api.smpp.message;

public class OptionalParameter {

    private int    optionalParameterTag;
    private byte[] optionalParameterData;

    public OptionalParameter() {
    }

    public int getOptionalParameterTag() {
        return this.optionalParameterTag;
    }

    public void setOptionalParameterTag(final int optionalParameterTag) {
        this.optionalParameterTag = optionalParameterTag;
    }

    public byte[] getOptionalParameterData() {
        return this.optionalParameterData;
    }

    public void setOptionalParameterData(final byte[] optionalParameterData) {
        this.optionalParameterData = optionalParameterData;
    }

}
