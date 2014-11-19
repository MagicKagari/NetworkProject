package socs.network.node;

import socs.network.message.LSAAbstract;
import socs.network.message.SOSPFPacket;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Client extends Reactor {

  //the router this client belongs to
  Router router;

  //process address to real socket channel
  private HashMap<String, SocketChannel> processAddressToSocketChannel = null;

  public Client(Router r) {
    router = r;
    lsd = r.lsd;
    writeBuffers = router.writeBuffers;
    readBuffers = router.readBuffers;
    processAddressToSocketChannel = new HashMap<String, SocketChannel>();
    //TODO: create a new selector to handle client IO tasks
    //or you can share selector with router...
    // if you choose the second option, you have to modify the code in Router
    // (don't need to start two threads)
  }

  /**
   * send the message to the server
   * @param lsa the message to send to the remote router
   * @param serverIP the process IP address of the remote router
   * @param serverPort the process port of the remote router
   */
  public void sendMessage(SOSPFPacket lsa, String serverIP, short serverPort) {
    try {
      String key = serverIP + ":" + serverPort;
      System.out.println("sending message to " + key);
      SocketChannel sc = processAddressToSocketChannel.get(key);
      if (sc == null) {
        throw new RuntimeException("No link to " + key);
      }
      // TODO: save the data in writeBuffer and wakeup the selector

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * finish the connection between the client and the remote router in client end
   * @param key the selectionKey of the corresponding SocketChannel
   */
  private void finishConnection(SelectionKey key) {
    try {
      //TODO: finish connection between the client and the server in client end
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("cannot connect to the server");
      System.exit(1);
    }
  }

  /**
   * connect to the remote router
   * @param serverIP the process IP address of the remote router
   * @param serverPort the process port of the remote router
   */
  void connectTo(String serverIP, short serverPort) {
    try {
      //TODO: open a new socketchannel to remote router
      //NOTE: you have to save the socketChannel in processAddressToSocketChannel
      // and register the socketChannel in selector thread (otherwise you will meet deadlock)
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void registerChannels() {
    try {
      //TODO: register the socketChannel
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      if (selector == null) {
        System.out.println("FAULT: you have to initialize the selector first");
        System.exit(1);
      }
      while (true) {
        //TODO: call selector.select() and call different handler based
        // on the channel's status
        // remember to register the channels before you take the selected keys
        // to ensure that selector.select() and register are called in the same thread
        // otherwise, you will fall into the deadlock
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void processDD(SOSPFPacket receivedDD, SelectionKey key) {
    //TODO: process the data description packet from the remote router
  }

  @Override
  protected void processLSAUpdate(SOSPFPacket pkt, SelectionKey key) {
    //TODO: process the LSA update packet from the remote router
  }

  @Override
  protected void processLSAReq(SOSPFPacket receivedLSAReq, SelectionKey key) {
    //TODO: process the LSA request packet from teh remote router
  }

  @Override
  protected void processHello(SOSPFPacket hello, SelectionKey key) {
    //TODO: process the Hello packet from the remote router
  }

}
