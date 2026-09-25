# Flock Simulator

A 2D boids flocking simulation written in Scala 3 with ScalaFX. The boids follow Craig Reynolds' three rules (cohesion, separation and alignment), steer away from the window edges and avoid circular obstacles.

## Requirements

- JDK 17 or newer
- [sbt](https://www.scala-sbt.org/download/)

## Running

```sh
sbt run
```

You can also open the project in IntelliJ IDEA with the Scala plugin and run `Launcher`.

## Controls

The sliders under the canvas change the flock's behavior while it runs:

| Control      | Effect                                   |
|--------------|------------------------------------------|
| Cohesion     | How strongly boids steer toward the center of nearby boids |
| Separation   | How strongly boids keep their distance from each other    |
| Alignment    | How strongly boids match their neighbors' heading          |
| Visual Range | How far a boid can see its neighbors                        |
| Trace Path   | Draws a short trail behind each boid                        |

## Data files

The app reads these files from the directory you start it in:

| File                      | Format (one entry per line)            |
|---------------------------|----------------------------------------|
| `obstacles.txt`           | `x y radius`                           |
| `boids_positions.txt`     | `x y vx vy`, starting position and velocity of each boid |
| `simulation_settings.txt` | `name = value` for `cohesionWeight`, `separationWeight`, `alignmentWeight`, `visualRange` |

Up to 100 boids are loaded from `boids_positions.txt`. If the file is empty, 100 boids start at random positions.

## Project structure

```
src/main/scala/
  Main.scala      window, controls, animation loop, file loading/saving
  Launcher.scala  entry point that starts Main
  Flock.scala     the collection of boids
  Boid.scala      steering rules and drawing for one boid
  Obstacle.scala  circular obstacle
  Vector2D.scala  2D vector math
```
