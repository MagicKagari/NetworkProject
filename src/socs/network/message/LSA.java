package socs.network.message;

import java.io.Serializable;
import java.util.LinkedList;

public class LSA implements Serializable {

  public String linkStateID;
  public String advRouter;
  public short lsaAge;
  public int lsaSeqNumber = Integer.MIN_VALUE;

  public LinkedList<LinkDescription> links = new LinkedList<LinkDescription>();

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(linkStateID + ":").append(advRouter + ",").
            append(lsaAge + ",").append(lsaSeqNumber + "\n");
    for (LinkDescription ld : links) {
      sb.append(ld);
    }
    sb.append("\n");
    return sb.toString();
  }
}
