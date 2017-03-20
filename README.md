# Project-Escape
Pacman
TODO
1) library vaja tööle saada (slick2d) - kõik peavad tegelma
2) server/client (mängija)

3) maze, food,
 pacman
4) ghosts(basic ai(liigu lähima suunas vb pisut randomit ka)), spawns,
coop? pvp?
powerups

kui aega üle siis:
random maze
parem ai
procedural maze
lag resistance

INSTALL GUIDE

1) pull latest version.

Intellij:
2) add src/main/java to sources root if it isnt
3) edit main file configs
4) Open View/Tool Windows/Maven projects
5) Lifecycle/compile and Lifecycle/package are used for building
6) Output are target/pacman.jar and targe/pacman-server.jar
7) to VM options add: "-Djava.library.path="target/natives"

Command line:
2) mvn compile
3) mvn package
4) java -jar target/pacman-server.jar
5) java -Djava.library.path="target/natives -jar target/pacman.jar
