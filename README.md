VersaTile Self-Assembly Simulator
=========

VersaTile is a cross-platform simulator for various models of algorithmic self-assembly.

It was written by the [Algorithmic Self-Assembly Research Group](http://faculty.utpa.edu/orgs/asarg/) at the University of Texas - Pan American.

The models it currently supports are:

  - abstract Tile Assembly Model
  - dupled Tile Assembly Model
  - hex Tile Assembly Model
  - polyomino Tile Assembly Model
  
  
It also supports:

  - temperature programming
  - concentration programming
  - negative interactions (attachments only)
  - a run-time model
  - flexible glues
  
It allows for:

 - 'Playing' through the assembly process
 - 'Rewinding' the assembly process
 - 'Fast-Forwarding'
 - 'Stepping through' the assembly process
 - 'Zooming' and 'Panning'
 - Creation of Tile Sets through our GUI editor
 - Visualization of the 'frontier'


VersaTile is currently under rapid-development and is in pre-alpha.


Version
----

pre-alpha

Examples
-----------

Polyomino contains a number of examples in the *com.asarg.polysim.examples* package:

* *TetrisSimulation.java* - Basic example showing colored polyomino tiles
* *RNGUnBoundedSimulation.java* - Example showing selection of random number generation using the ATAM model