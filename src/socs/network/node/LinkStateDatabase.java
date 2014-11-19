package socs.network.node;

import socs.network.message.LSA;
import socs.network.message.LSAAbstract;
import socs.network.message.LinkDescription;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class LinkStateDatabase {

  //linkID => LSAInstance
  private HashMap<String, LSA> _store = new HashMap<String, LSA>();

  private RouterDescription rd = null;

  public LinkStateDatabase(RouterDescription routerDescription) {
    rd = routerDescription;
    LSA l = initLinkStateDatabase();
    _store.put(l.linkStateID, l);
  }

  public LSA getLSA(String IP) {
    return _store.get(IP);
  }

  public void addLSA(LSA lsa) {
    if (!_store.containsKey(lsa.linkStateID) ||
            _store.get(lsa.linkStateID).lsaSeqNumber <= lsa.lsaSeqNumber) {
      _store.put(lsa.linkStateID, lsa);
    } else {
      System.out.println("discard " + lsa);
    }
  }

  private void dijkstra() {
    //TODO: you have to calculate the path to each node and save it in a proper data
    // structure
  }

  public Vector<LSA> getRequestedLSA(Collection<LSAAbstract> requestedLSAAbstracts) {
    Vector<LSA> ret = new Vector<LSA>();
    for (LSAAbstract lsaa : requestedLSAAbstracts) {
      if (_store.containsKey(lsaa.linkStateID)) {
        if (_store.get(lsaa.linkStateID).lsaSeqNumber <= lsaa.linkStateSeq) {
          ret.add(_store.get(lsaa.linkStateID));
        }
      } else {
        ret.add(_store.get(lsaa.linkStateID));
      }
    }
    return ret;
  }

  /**
   * called when sending reply to lsa request
   * @param reportedLSA
   * @return
   */
  public Vector<LSAAbstract> getRequestedLSAAbstracts(Collection<LSAAbstract> reportedLSA) {
    Vector<LSAAbstract> ret =  new Vector<LSAAbstract>();
    for (LSAAbstract la: reportedLSA) {
      if (_store.containsKey(la.linkStateID)) {
        if (_store.get(la.linkStateID).lsaSeqNumber <= la.linkStateSeq) {
          ret.add(la);
        }
      } else {
        ret.add(la);
      }
    }
    return ret;
  }

  /**
   * usually called when sending DD
   * @return
   */
  public Vector<LSAAbstract> getLSAAbstracts() {
    Vector<LSAAbstract> lsas = new Vector<LSAAbstract>();
    for (LSA lsa: _store.values()) {
      LSAAbstract lsaa = new LSAAbstract();
      lsaa.linkStateID = lsa.linkStateID;
      lsaa.linkStateSeq = lsa.lsaSeqNumber;
      lsas.add(lsaa);
    }
    return lsas;
  }

  //initialize the linkstate database by adding an entry about the router itself
  private LSA initLinkStateDatabase() {
    LSA lsa = new LSA();
    lsa.linkStateID = rd.simulatedIPAddress;
    System.out.println("====" + lsa.linkStateID);
    lsa.advRouter = rd.simulatedIPAddress;
    lsa.lsaSeqNumber = Integer.MIN_VALUE;
    lsa.lsaAge = 0;
    LinkDescription ld = new LinkDescription();
    ld.linkID = rd.simulatedIPAddress;
    ld.portNum = -1;
    ld.tosMetrics = 0;
    lsa.links.add(ld);
    return lsa;
  }


  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (LSA lsa: _store.values()) {
      sb.append(lsa.linkStateID).append("(" + lsa.lsaSeqNumber + ")").append(":\t");
      for (LinkDescription ld : lsa.links) {
        sb.append(ld.linkID).append(",").append(ld.portNum).append(",").
                append(ld.tosMetrics).append("\t");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

}
