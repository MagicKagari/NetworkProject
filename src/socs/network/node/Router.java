package socs.network.node;

import socs.network.message.LSA;
import socs.network.message.LSAAbstract;
import socs.network.message.LinkDescription;
import socs.network.message.SOSPFPacket;
import socs.network.util.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


public class Router extends Reactor {

  //assuming that all routers are with 4 ports
  Link[] ports = new Link[4];
  private int validPortNum = 0;
  HashMap<String, Integer> remoteIPtoPortNum = new HashMap<String, Integer>();

  private Client client = null;

  public Router(Configuration config) {
    rd.simulatedIPAddress = config.getString("socs.network.router.ip");
    lsd = new LinkStateDatabase(rd);
    writeBuffers = new ConcurrentHashMap<SocketChannel, ArrayList<SOSPFPacket>>();
    readBuffers = new ConcurrentHashMap<SocketChannel, ChannelState>();
    client = new Client(this);
    initServerSocket();
  }

  private boolean initServerSocket() {
    try {
      //TODO: init the ServerSocketChannel
      //NOTE: read the example in the slides
      return true;
    } catch (Exception oe) {
      return false;
    }
  }

  private void accept(SelectionKey key) throws IOException {
    //TODO: accept the connection and init the write and read buffer
  }

  @Override
  public void run() {
    Thread t1 = new Thread(new ServerGo());
    t1.start();
    Thread t2 = new Thread(new ClientGo());
    t2.start();
  }

  private class ClientGo implements Runnable {
    @Override
    public void run() {
      try {
        client.run();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private class ServerGo implements Runnable {

    @Override
    public void run() {
      if (selector == null) {
        System.out.println("FAULT: you have to initialize the selector first");
        System.exit(1);
      }
      try {
        while (true) {
          selector.select();
          Set selected = selector.selectedKeys();
          for (Object aSelected : selected) {
            //accept the connection
            SelectionKey selectkey = (SelectionKey) aSelected;
            if (selectkey.isAcceptable()) {
              accept(selectkey);
            } else {
              if (selectkey.isReadable()) {
                read(selectkey);
              } else {
                if (selectkey.isWritable()) {
                  write(selectkey);
                }
              }
            }
          }
          selected.clear();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void processAttach(String processIP, short processPort,
                             String simulatedIP, short weight) {
    //TODO: process the attach command
    //Hint: call client.connect()
  }

  private void processDisconnect(short portNum) {
    //TODO: process the disconnect command
    // shutdown the connection of port PortNum
  }

  private void processDetect(String dst_ip) {
    //TODO: process the detect command
    //print out the shortest path from this router to dst_ip
  }

  public void terminal() {
    try {
      run();
      InputStreamReader isReader = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isReader);
      System.out.print(">> ");
      String command = br.readLine();
      while (true) {
        if (command.startsWith("detect ")) {
          String[] cmdLine = command.split(" ");
          processDetect(cmdLine[1]);
        } else {
          if (command.startsWith("disconnect ")) {
            String[] cmdLine = command.split(" ");
            processDisconnect(Short.parseShort(cmdLine[1]));
          } else {
            if (command.startsWith("quit")) {
              break;
            } else {
              if (command.startsWith("attach ")) {
                if (validPortNum >= 4) {
                  System.out.println("the router's ports has been full");
                } else {
                  String[] cmdLine = command.split(" ");
                  processAttach(cmdLine[1], Short.parseShort(cmdLine[2]),
                          cmdLine[3], Short.parseShort(cmdLine[4]));
                }
              } else {
                if (command.equals("start")) {
                  //TODO: broadcast the Hello

                }
              }
            }
          }
        }
        System.out.print(">> ");
        command = br.readLine();
      }
      isReader.close();
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
