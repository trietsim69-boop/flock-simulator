class Obstacle(val x: Double, val y: Double, val radius: Double):
  def isInside(position: Vector2D): Boolean =
    position.distanceTo(Vector2D(x, y)) < radius
