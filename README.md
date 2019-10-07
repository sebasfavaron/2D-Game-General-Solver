This is a university project done in collaboration with Ximena Zuberbuhler, Martin Victory and Segundo Fariña. The objective was to implement a 2D tabletop game general solving framework and use it to solve the game [Skyscrapers](https://www.conceptispuzzles.com/index.aspx?uri=puzzle/skyscrapers/).

# Skycrapers game

## Compiling process
This project is based on maven, so to build a `jar` just run the following code in the main folder.
```
$ mvn clean package
```
The generated `jar` will be placed under the folder `TP1/target/`

## Running the binaries
To run this project just run the following code:
```
$ java -jar [path to jar] [list of arguments]
```
There is an already builded jar in the folder `Binaries/`

## Arguments
For the program to run it must recieve 5 arguments in the following order:

* Number of times to run: (int)
* Path to input file for problem definition: (String)
* Path to output file: (String)
* Algorithm used: [bfs | dfs | iddfs | greedy | astar]
* Heuristic used: [A | B]

Example arguments:
```
1 classes/3by3Game.json output.txt bfs A
```

## Input files
There are some sample boards already loaded in the `Binaries/` folder.

## Custom Input file 
To set the solver parameters you need to provide the program with a JSON file 
in the following format:

````
{
    "size": 5, 
    "restrictions: {
        "up": [3,4,1,3,2],
        "right": [2,1,5,2,2],
        "down": [3,4,1,3,2],
        "left": [2,1,3,2,2]
     }
}
````

The size parameteres indicates the length of the board, the restrictions are
 the restrictions applied to the board each array **must** be of length equal to 
 the board size, and values of the array **must** be between *0* and *size* where *0*
 indicates that there is no restiction in that position.
