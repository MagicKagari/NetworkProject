package socs.network.node;

import java.nio.ByteBuffer;

public class ChannelState {
  ByteBuffer readBuffer = ByteBuffer.allocate(8192);
  int hasReadBytes = 0;
}
