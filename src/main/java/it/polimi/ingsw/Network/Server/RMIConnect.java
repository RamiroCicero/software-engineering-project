package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Network.Client.Communication.CommunicationProtocol;
import it.polimi.ingsw.Network.Messages.Message;

import java.rmi.RemoteException;

/**
 * The RMIConnect class represents a connection implementation for the RMI protocol.
 * It implements the Connection interface.
 */
public class RMIConnect implements Connection {
    private final CommunicationProtocol protocol;
    private Long UID;
    private String nickname;

    /**
     * Constructs a new RMIConnect instance with the specified communication protocol, UID, and nickname.
     *
     * @param protocol the communication protocol to use for the connection
     * @param UID      the unique identifier (UID) assigned to the connection
     * @param nickname the nickname associated with the client
     */
    public RMIConnect(CommunicationProtocol protocol, Long UID, String nickname){
        this.protocol = protocol;
        this.UID = UID;
        this.nickname = nickname;
    }



    /**
     * Sends a message using the underlying communication protocol.
     *
     * @param message the message to be sent
     */
    public void sendMessage(Message message){
        try {
            protocol.onMessage(message);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Long getUID() {
        return UID;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
