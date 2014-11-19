package socs.network.message;

import java.io.*;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class SOSPFPacket implements Serializable {

  // facilitate the debugging
  public static AtomicInteger sospfPacketID = new AtomicInteger(0);

  public int packetID = sospfPacketID.getAndIncrement();

  //for inter-process communication
  public String srcProcessIP;
  public short srcProcessPort;

  // simplified IP header
  public String srcIP;
  public String dstIP;

  //common header
  public short sospfType;
  public String routerID;

  //hello
  public String neighborID;

  //Database Description
  public boolean masterSlaveFlag = false;
  public Vector<LSAAbstract> abstractArray = null;//new Vector<LSAAbstract>();

  public Vector<LSA> lsaArray = null;

  public Vector<String> lsaAckArray = new Vector<String>();

  public static SOSPFPacket deserialize(byte[] data) {
    try {
      ByteArrayInputStream in = new ByteArrayInputStream(data);
      ObjectInputStream is = new ObjectInputStream(in);
      SOSPFPacket ret = (SOSPFPacket) is.readObject();
      is.close();
      return ret;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static byte[] serialize(SOSPFPacket obj) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(out);
      os.writeObject(obj);
      return out.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
