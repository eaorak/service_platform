package com.adenon.smpp.server.handlers;

import java.nio.ByteBuffer;

import com.adenon.api.smpp.common.CommonUtils;
import com.adenon.api.smpp.common.SmppApiException;
import com.adenon.api.smpp.logging.LoggerWrapper;
import com.adenon.api.smpp.message.BindRequestMessage;
import com.adenon.api.smpp.message.BindResponseMessage;
import com.adenon.api.smpp.message.MessageHeader;
import com.adenon.api.smpp.sdk.ConnectionInformation;
import com.adenon.smpp.server.callback.response.BindResponse;
import com.adenon.smpp.server.callback.response.EBindResult;
import com.adenon.smpp.server.core.ServerApiDelegator;
import com.adenon.smpp.server.core.ServerIOReactor;
import com.adenon.smpp.server.managers.ServerLogManager;


public class BindHandler {

    public void handle(final MessageHeader smpp34Header,
                       final ByteBuffer byteBuffer,
                       final ServerIOReactor ioReactor) throws Exception {
        if (ioReactor.getLogger().isDebugEnabled()) {
            ioReactor.getLogger().debug("BindHandler",
                                        "handle",
                                        smpp34Header.getSequenceNo(),
                                        ioReactor.getLabel(),
                                        " Bind recieved : " + smpp34Header.getCommandID() + " . Handling binding process.");
        }

        BindRequestMessage bindRequestMessage = new BindRequestMessage();
        bindRequestMessage.setIp(ioReactor.getConnectionInformation().getIp());
        bindRequestMessage.parseMessage(byteBuffer);

        if (ioReactor.getLogger().isDebugEnabled()) {
            ioReactor.getLogger().debug("BindHandler",
                                        "handle",
                                        smpp34Header.getSequenceNo(),
                                        ioReactor.getLabel(),
                                        " BindMessage decoded : " + bindRequestMessage.toString());
        }


        BindResponse bindResponse = ioReactor.getServerCallback().bindReceived(bindRequestMessage);
        if (bindResponse == null) {
            throw new SmppApiException(SmppApiException.NULL, SmppApiException.DOMAIN_SMPP_SERVER, "Bind Response is null");
        }
        if (ioReactor.getLogger().isDebugEnabled()) {
            ioReactor.getLogger().debug("BindHandler",
                                        "handle",
                                        smpp34Header.getSequenceNo(),
                                        ioReactor.getLabel(),
                                        " Bind result : "
                                                + bindResponse.getBindResult().getValue()
                                                + " desc : "
                                                + bindResponse.getBindResult().getDescription());
        }

        BindResponseMessage bindResponseMessage = new BindResponseMessage();
        bindResponseMessage.setSystemId(bindRequestMessage.getSystemIdentifier());

        ioReactor.sendBindResponse(smpp34Header, byteBuffer, bindResponseMessage, bindResponse.getBindResult().getValue());

        if (bindResponse.getBindResult() != EBindResult.BindSuccess) {
            throw new SmppApiException(SmppApiException.LOGIN_ERROR, SmppApiException.DOMAIN_SMPP_SERVER, "Bind Error");
        }

        ioReactor.getSmppApiDelegator().getServerConnectionStore().add(bindResponse.getConnectionName(), ioReactor);
        ioReactor.setExternalConnectionName(bindResponse.getConnectionName());

        ioReactor.setBindType(smpp34Header.getCommandID());
        ioReactor.getBinded().set(true);

        ConnectionInformation connectionInformation = ioReactor.getConnectionInformation();
        connectionInformation.getConnectionState().idle();
        String serverName = connectionInformation.getConnectionGroupName();

        String connectionName = "Unknown";
        if (CommonUtils.checkStringIsEmpty(bindResponse.getConnectionName())) {
            connectionName = bindResponse.getConnectionName() + "-" + ioReactor.getConnectionInformation().getIp();
        } else {
            connectionName = bindRequestMessage.getSystemIdentifier()
                             + "_"
                             + bindRequestMessage.getUsername()
                             + "-"
                             + ioReactor.getConnectionInformation().getIp();
        }
        ioReactor.setLabel("[" + serverName + "@" + connectionName + "]");
        ioReactor.getConnectionInformation().setConnected(true);
        ioReactor.getConnectionInformation().setConnectionName(connectionName);
        ioReactor.getConnectionInformation().setUserName(bindRequestMessage.getUsername());
        ServerApiDelegator smppApiDelegator = ioReactor.getSmppApiDelegator();
        ServerLogManager logManager = smppApiDelegator.getLogManager();
        LoggerWrapper clogger;
        switch (logManager.getLogType()) {
            case LogAllInOneFile:
                clogger = logManager.getLogger();
                break;
            case LogConnectionGroupSeparetly:
                clogger = logManager.getLogControler().getLogger(smppApiDelegator.getServerName());
                break;
            case LogConnectionsSeparetly:
                clogger = logManager.getLogControler().getLogger(ioReactor.getConnectionInformation().getConnectionName());
                break;
            default:
                clogger = logManager.getLogger();
                break;
        }
        ioReactor.setLogger(clogger);
    }
}
