package com.adenon.channel.sss.api.message;

import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.MessageType;

public class SmsSendMessage extends AbstractMessage {

    private String  groupName;
    private String  connectionGroupName;
    private String  connectionName;
    private String  message;
    private int     dataCoding;
    private String  sourceNumber;
    private int     sourceTon;
    private int     sourceNpi;
    private String  destinationNumber;
    private int     destinationTon;
    private int     destinationNpi;
    private boolean requestDeliver;
    private int     transactionId;

    public SmsSendMessage() {
        super(MessageType.BEGIN);
    }

    @Override
    public SmsMessageTypes getId() {
        return SmsMessageTypes.SMS_MESSAGE_DELIVER;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }

    public String getConnectionGroupName() {
        return this.connectionGroupName;
    }

    public void setConnectionGroupName(final String clientName) {
        this.connectionGroupName = clientName;
    }

    public String getConnectionName() {
        return this.connectionName;
    }

    public void setConnectionName(final String hostName) {
        this.connectionName = hostName;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public int getDataCoding() {
        return this.dataCoding;
    }

    public void setDataCoding(final int dataCoding) {
        this.dataCoding = dataCoding;
    }

    public String getSourceNumber() {
        return this.sourceNumber;
    }

    public void setSourceNumber(String sourceNumber) {
        this.sourceNumber = sourceNumber;
    }

    public int getSourceTon() {
        return this.sourceTon;
    }

    public void setSourceTon(int sourceTon) {
        this.sourceTon = sourceTon;
    }

    public int getSourceNpi() {
        return this.sourceNpi;
    }

    public void setSourceNpi(int sourceNpi) {
        this.sourceNpi = sourceNpi;
    }

    public String getDestinationNumber() {
        return this.destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public int getDestinationTon() {
        return this.destinationTon;
    }

    public void setDestinationTon(int destinationTon) {
        this.destinationTon = destinationTon;
    }

    public int getDestinationNpi() {
        return this.destinationNpi;
    }

    public void setDestinationNpi(int destinationNpi) {
        this.destinationNpi = destinationNpi;
    }

    public boolean isRequestDeliver() {
        return this.requestDeliver;
    }

    public void setRequestDeliver(boolean requestDeliver) {
        this.requestDeliver = requestDeliver;
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

}
