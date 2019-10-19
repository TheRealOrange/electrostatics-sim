package canvas;

import math.Vector2D;

public interface CanvasNode {
  void reposition(Vector2D prevOffset, Vector2D offset);
}
