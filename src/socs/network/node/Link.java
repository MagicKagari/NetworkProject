package socs.network.node;

public class Link {

  RouterDescription router1;
  RouterDescription router2;

  public Link(RouterDescription r1, RouterDescription r2) {
    router1 = r1;
    router2 = r2;
  }

  public RouterDescription getOtherEnd(RouterDescription r) {
    if (router1 == r) {
      return router2;
    } else {
      if (router2 == r) {
        return router1;
      } else {
        throw new RuntimeException("tried to get the other end in a wrong link");
      }
    }
  }
}
