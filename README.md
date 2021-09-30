# comp512-project

### Basic Setup
Have three terminals open:
1. For the Client, in that terminal run something like: `ssh eitovi@lab2-18`
2. For the Server, in that terminal run something like: `ssh eitovi@lab2-23`
3. For file transfer, from the root folder run something like: `scp -r ./Template eitovi@lab2-23.cs.mcgill.ca:/home/2018/eitovi/Desktop/Comp512/A1`

### Running RMI

### Server
From the Server terminal cd into /Server then run: `./run_servers.sh`. This will open 4 windows where you will need to enter your McGill CS password. To switch between the screens press `ctrl b` followed by an arrow key. To close the windows press `ctrl b` followed by `&`.

### Client
Then from the Client terminal cd into /Client then run something like: `./run_client.sh lab2-23` Where lab2-23 is the host of your middleware (It should be 23). If everything is set up correctly this should start the client and connect it to the middleware. 

From there you can run the `help` command to see the list of commands and `help, COMMAND` to see how to use a specific command. Ensure that all arguments are separated by commas and for arguments that want a Y/N use true/false

### Running TCP
Do the same as RMI Except instead of `./run_servers.sh` and `./run_client.sh` use `./run_servers_tcp.sh` and `./run_client_tcp.sh`

### Original README 

To run the RMI resource manager:

```
cd Server/
./run_server.sh [<rmi_name>] # starts a single ResourceManager
./run_servers.sh # convenience script for starting multiple resource managers
```

To run the RMI client:

```
cd Client
./run_client.sh [<server_hostname> [<server_rmi_name>]]
```