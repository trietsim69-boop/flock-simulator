import scalafx.scene.canvas.GraphicsContext
import scala.collection.mutable.ListBuffer

class Flock:
  val boids: ListBuffer[Boid] = ListBuffer()
  var tracePath: Boolean = false

  def populate(n: Int, width: Double, height: Double): Unit =
    for (_ <- 1 to n) do
      val pos = Vector2D(math.random() * width, math.random() * height)
      val vel = Vector2D(math.random() * 4 - 2, math.random() * 4 - 2)
      val boid = new Boid(pos, vel)
      boid.drawTrail = tracePath
      boids += boid


  def update(width: Double, height: Double, obstacles: List[Obstacle]): Unit =
    boids.foreach(_.drawTrail = tracePath)
    boids.foreach(_.update(boids.toList, obstacles, width, height))

  def draw(gc: GraphicsContext, obstacles: List[Obstacle]): Unit =
    boids.foreach(_.draw(gc))
    obstacles.foreach(_.draw(gc))

  def setWeights(coh: Double, sep: Double, ali: Double, range: Double): Unit =
    for (boid <- boids) do
      boid.cohesionWeight = coh
      boid.separationWeight = sep
      boid.alignmentWeight = ali
      boid.visualRange = range
  