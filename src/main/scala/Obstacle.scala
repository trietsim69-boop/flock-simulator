import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color

class Obstacle(val x: Double, val y: Double, val radius: Double):
  def isInside(position: Vector2D): Boolean =
    position.distanceTo(Vector2D(x, y)) < radius
  def draw(gc: GraphicsContext): Unit =
    gc.fill = Color.Red
    gc.fillOval(x - radius, y - radius, radius * 2, radius * 2)