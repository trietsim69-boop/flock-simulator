case class Vector2D(x: Double, y: Double):
  def +(other: Vector2D): Vector2D = Vector2D(x + other.x, y + other.y)
  def -(other: Vector2D): Vector2D = Vector2D(x - other.x, y - other.y)
  def *(scalar: Double): Vector2D = Vector2D(x * scalar, y * scalar)
  def /(scalar: Double): Vector2D = Vector2D(x / scalar, y / scalar)

  def magnitude: Double = math.sqrt(x * x + y * y)
  def normalize: Vector2D = if (magnitude == 0) Vector2D(1, 0) else this / magnitude
  def limit(max: Double): Vector2D = if (magnitude > max) normalize * max else this
  def distanceTo(other: Vector2D): Double = (this - other).magnitude
  def dot(other: Vector2D): Double = x * other.x + y * other.y
  def angleBetween(other: Vector2D): Double =
    val cosTheta = (this dot other) / (this.magnitude * other.magnitude)
    math.acos(math.max(-1.0, math.min(1.0, cosTheta)))
  def rotate(angle: Double): Vector2D =
    val cos = math.cos(angle)
    val sin = math.sin(angle)
    Vector2D(x * cos - y * sin, x * sin + y * cos)

