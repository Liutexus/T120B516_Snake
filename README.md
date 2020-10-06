## Multiplayer Snake

### Project description
Multiplayer Snake project is in early development stage dedicated for University's module assignment.

## Repository structure
### /src/client:
Source code folder for storing logic which is executed by the client. Client constantly communicates with the server and updates it's user interface and sends control messages accordingly.

### /src/server:
Source code folder for storing logic with is executed by the server. Server opens sockets for every client and communicates through them. Server provides with unique ID for every client and creates an object to store client's statuses. Server listens to clients' messages and modifies data accordingly.

### /libs:
Library folder for storing dependencies of the project. This folder is meant to be small as the one of the requirements for the project given by University is to use as minimum amount of libraries as possible.

### Dependencies:
As for now, the only required dependency are Jackson's Core, Annotations and Databind modules.

## Running the application
There are a couple of ways so start the application:
* Download the source code and compile the binaries via compiler or IDE.
* Download and run the already compiled binaries.

## Note from the dev
The game is in very early state and developed by very inexperienced programmers, so bad practices, unreadable code and entangled logic will take place in project's code. Parental advisory is suggested.