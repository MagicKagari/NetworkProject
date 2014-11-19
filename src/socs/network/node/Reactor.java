package socs.network.node;

import socs.network.message.LSA;
import socs.network.message.SOSPFPacket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Reactor {

  protected ConcurrentHashMap<SocketChannel, ArrayList<SOSPFPacket>> writeBuffers = null;

  protected ConcurrentHashMap<SocketChannel, ChannelState> readBuffers = null;

  protected Selector selector;
  protected LinkStateDatabase lsd;

  RouterDescription rd = new RouterDescription();

  protected void write(SelectionKey writableKey) {
    SocketChannel sc = (SocketChannel) writableKey.channel();
    try {
      //TODO: write data to sc
      //NOTE: you may want to write the message size before you write the real
      //message, otherwise, it's hard to judge whether the buffer contains complete
      //message when reading...(Read the slides)
    } catch (Exception e) {
      closeChannel(sc);
      e.printStackTrace();
    }
  }

  void read(SelectionKey selectedKey) {
    SocketChannel socketChannel = (SocketChannel) selectedKey.channel();
    try {
      //TODO: first update the current key and corresponding ChannelState
      ChannelState cs = readBuffers.get(socketChannel);
      int readbytes = socketChannel.read(cs.readBuffer);
      if (readbytes == -1) {
        throw new Exception("end reading");
      }

      //TODO: read the buffers of all socketChannel,
      //NOTE: remember to check whether the buffer contains complete message
      // take use of hasReadBytes in ChannelState
    } catch (Exception e) {
      e.printStackTrace();
      closeChannel(socketChannel);
    }
  }

  void closeChannel(SocketChannel sc) {
    try {
      if (sc != null) {
        readBuffers.remove(sc);
        writeBuffers.remove(sc);
        sc.keyFor(selector).cancel();
        sc.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void dispatch(SOSPFPacket inputPacket, SelectionKey key) {
    if (inputPacket == null) {
      System.out.println("WARNING: read null message in dispatch");
      selector.wakeup();
      return;
    }
    switch (inputPacket.sospfType) {
      case 1:
        processHello(inputPacket, key);
        break;
      case 2:
        processDD(inputPacket, key);
        break;
      case 3:
        processLSAReq(inputPacket, key);
        break;
      case 4:
        processLSAUpdate(inputPacket, key);
        break;
      case 5:
        processLSAAck(inputPacket, key);
      default:
        System.out.println("Unknown message " + inputPacket.sospfType + " from " + inputPacket.srcIP);
        break;
    }
  }

  //TODO: the main service function of the reactor class (Client and Router)
  public abstract void run();

  // TODO: you can override the following 5 functions in Router and Client to define the
  // FSM of our protocol
  protected void processHello(SOSPFPacket pkt, SelectionKey key) {

  }

  protected void processDD(SOSPFPacket pkt, SelectionKey key) {

  }

  protected void processLSAReq(SOSPFPacket receivedLSAReq, SelectionKey key) {

  }

  protected void processLSAUpdate(SOSPFPacket pkt, SelectionKey key) {

  }

  protected void processLSAAck(SOSPFPacket pkt, SelectionKey key) {

  }
}
