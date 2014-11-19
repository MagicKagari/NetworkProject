package socs.network.node;

public enum RouterStatus {
  INIT,
  TWO_WAY,
  // starting from here, the status are for database synchronization
  EXSTART,
  EXCHANGE,
  LOADING,
  FULL
}
