import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color
import scala.collection.mutable.ArrayBuffer

class Boid(var position: Vector2D, var velocity: Vector2D):
  var cohesionWeight = 0.01
  var separationWeight = 0.1
  var alignmentWeight = 0.05
  var visualRange = 100.0
  val maxSpeed = 4.0
  val minSpeed = 2.0
  val edgeMargin = 50.0
  val edgeTurnFactor = 0.5
  var drawTrail = false
  private val history = ArrayBuffer[Vector2D]()

  def update(flock: List[Boid], width: Double, height: Double): Unit =
    val neighbors = flock.filter(b => b != this && position.distanceTo(b.position) < visualRange)

    val cohesionVec = rule1(neighbors) * cohesionWeight
    val separationVec = rule2(neighbors) * separationWeight
    val alignmentVec = rule3(neighbors) * alignmentWeight
    val edgeVec = avoidEdges(width, height)
    val collisionAvoidanceVec = avoidCollisions(flock)
    val acceleration = cohesionVec + separationVec + alignmentVec + edgeVec + collisionAvoidanceVec

    velocity = (velocity + acceleration).limit(maxSpeed)
    if (velocity.magnitude < minSpeed) velocity = velocity.normalize * minSpeed

    position += velocity

    if (drawTrail) then
      history.append(position)
      if (history.size > 30) history.remove(0)

  private def rule1(neighbors: List[Boid]): Vector2D =
    if (neighbors.nonEmpty)
      val center = neighbors.map(_.position).reduce(_ + _) / neighbors.size
      (center - position).normalize * 0.5
    else
      Vector2D(0, 0)

  private def rule2(neighbors: List[Boid]): Vector2D =
    val close = neighbors.filter(b => position.distanceTo(b.position) < 20)
    if (close.nonEmpty) then
      close.map(b => (position - b.position).normalize / position.distanceTo(b.position)).reduce(_ + _).normalize
    else
      Vector2D(0, 0)

  private def rule3(neighbors: List[Boid]): Vector2D =
    if (neighbors.nonEmpty) then
      val avgVelocity = neighbors.map(_.velocity).reduce(_ + _) / neighbors.size
      (avgVelocity - velocity).normalize * 0.5
    else
      Vector2D(0, 0)

  private def avoidEdges(width: Double, height: Double): Vector2D =
    var steer = Vector2D(0, 0)
    if (position.x < edgeMargin) steer += Vector2D(edgeTurnFactor, 0)
    if (position.x > width - edgeMargin) steer += Vector2D(-edgeTurnFactor, 0)
    if (position.y < edgeMargin) steer += Vector2D(0, edgeTurnFactor)
    if (position.y > height - edgeMargin) steer += Vector2D(0, -edgeTurnFactor)
    steer

  private def avoidCollisions(flock: List[Boid]): Vector2D =
    var avoidVec = Vector2D(0, 0)
    flock.foreach { other =>
      if (other != this && position.distanceTo(other.position) < 30) then
        val directionToOther = other.position - position
        val distance = directionToOther.magnitude
        val predictedPosition = position + velocity
        val directionAway = (predictedPosition - other.position).normalize
        if (distance < 20) avoidVec += directionAway * (1.0 / distance)
    }
    avoidVec
  def draw(gc: GraphicsContext): Unit =
    if (drawTrail && history.nonEmpty) then
      gc.stroke = Color.web("#558cf488")
      gc.beginPath()
      gc.moveTo(history.head.x, history.head.y)
      history.foreach(p => gc.lineTo(p.x, p.y))
      gc.stroke()

    val angle = math.atan2(velocity.y, velocity.x)
    gc.save()
    gc.translate(position.x, position.y)
    gc.rotate(math.toDegrees(angle))
    gc.translate(-position.x, -position.y)

    gc.fill = Color.web("#558cf4")
    gc.beginPath()
    gc.moveTo(position.x, position.y)
    gc.lineTo(position.x - 15, position.y + 5)
    gc.lineTo(position.x - 15, position.y - 5)
    gc.lineTo(position.x, position.y)
    gc.fill()
    gc.restore()