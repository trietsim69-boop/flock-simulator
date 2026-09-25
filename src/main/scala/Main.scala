import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.Color
import scalafx.animation.AnimationTimer
import scalafx.scene.control.{Slider, Label, CheckBox}
import scalafx.scene.layout.{VBox, HBox}
import scalafx.geometry.Insets
import java.io.{File, PrintWriter}
import scala.io.Source

object Main extends JFXApp3:

  val screenWidth = 1000
  val screenHeight = 700
  val numBoids = 100

  val flock = new Flock()

  override def start(): Unit =
    val obstacles = loadObstacles("obstacles.txt")
    val loadedBoids = loadBoidPositions("boids_positions.txt")
    val (cohesionWeight, separationWeight, alignmentWeight, visualRange) = loadSimulationSettings("simulation_settings.txt")
    if (loadedBoids.isEmpty) then
      flock.populate(numBoids, screenWidth, screenHeight)
    else
      flock.boids.clear()
      flock.boids ++= loadedBoids.take(numBoids)

    flock.setWeights(cohesionWeight, separationWeight, alignmentWeight, visualRange)

    val canvas = new Canvas(screenWidth, screenHeight)
    val gc = canvas.graphicsContext2D

    val cohesionSlider = new Slider(0, 0.1, 0.005)
    val separationSlider = new Slider(0, 0.3, 0.1)
    val alignmentSlider = new Slider(0, 0.2, 0.05)
    val visualRangeSlider = new Slider(10, 200, 75)
    val traceCheck = new CheckBox("Trace Path")

    cohesionSlider.value = cohesionWeight
    separationSlider.value = separationWeight
    alignmentSlider.value = alignmentWeight
    visualRangeSlider.value = visualRange

    val controls = new HBox(10,
      new Label("Cohesion") { padding = Insets(5) }, cohesionSlider,
      new Label("Separation") { padding = Insets(5) }, separationSlider,
      new Label("Alignment") { padding = Insets(5) }, alignmentSlider,
      new Label("Visual Range") { padding = Insets(5) }, visualRangeSlider,
      traceCheck)

    controls.padding = Insets(10)

    val layout = new VBox()
    layout.children = Seq(canvas, controls)

    stage = new JFXApp3.PrimaryStage:
      title = "Flock Simulator"
      scene = new Scene(screenWidth, screenHeight + 60):
        content = layout

    AnimationTimer { _ =>
      gc.fill = Color.Black
      gc.fillRect(0, 0, screenWidth, screenHeight)

      flock.setWeights(
        cohesionSlider.value.value,
        separationSlider.value.value,
        alignmentSlider.value.value,
        visualRangeSlider.value.value
      )

      flock.tracePath = traceCheck.selected.value

      flock.update(screenWidth, screenHeight, obstacles)
      flock.draw(gc, obstacles)
    }.start()

    saveBoidPositions(flock.boids.toList, "boids_positions.txt")
    saveSimulationSettings(
      flock.boids.head.cohesionWeight,
      flock.boids.head.separationWeight,
      flock.boids.head.alignmentWeight,
      flock.boids.head.visualRange,
      "simulation_settings.txt"
    )

  def saveBoidPositions(boids: List[Boid], filename: String): Unit =
    val writer = new PrintWriter(new File(filename))
    boids.foreach(boid =>
      writer.println(s"${boid.position.x} ${boid.position.y} ${boid.velocity.x} ${boid.velocity.y}"))
    writer.close()

  def loadBoidPositions(filename: String): List[Boid] =
    val lines = Source.fromFile(filename).getLines().toList
    lines.map(line =>
      val parts = line.split(" ")
      val position = Vector2D(parts(0).toDouble, parts(1).toDouble)
      val velocity = Vector2D(parts(2).toDouble, parts(3).toDouble)
      new Boid(position, velocity)
    )

  def saveSimulationSettings(cohesionWeight: Double, separationWeight: Double, alignmentWeight: Double, visualRange: Double, filename: String): Unit =
    val writer = new PrintWriter(new File(filename))
    writer.println(s"cohesionWeight = $cohesionWeight")
    writer.println(s"separationWeight = $separationWeight")
    writer.println(s"alignmentWeight = $alignmentWeight")
    writer.println(s"visualRange = $visualRange")
    writer.close()

  def loadSimulationSettings(filename: String): (Double, Double, Double, Double) =
    val lines = Source.fromFile(filename).getLines().toList
    val settings = lines.map(line =>
      val parts = line.split(" = ")
      parts(0) -> parts(1).toDouble
    ).toMap

    (
      settings("cohesionWeight"),
      settings("separationWeight"),
      settings("alignmentWeight"),
      settings("visualRange")
    )

  def loadObstacles(filename: String): List[Obstacle] =
    val lines = Source.fromFile(filename).getLines().toList
    lines.map(line => {
      val parts = line.split(" ")
      new Obstacle(parts(0).toDouble, parts(1).toDouble, parts(2).toDouble)
    })
