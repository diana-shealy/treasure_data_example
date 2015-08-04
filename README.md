# treasure_data_example

Pull treasure_data_example directory from GitHub, all source code and jar dependencies needed are within the directory. If in doubt, you can download the Apache Commons CLI here: https://commons.apache.org/proper/commons-cli/download_cli.cgi

Save your `treasure-data.properties` file under `td-query\resources`

The script `td-run.sh` contains all commands you need to build, run and clean the program. 

Building the Program 
`./td-run.sh build` - Compiles and builds the class files

Running the Program 
`./td-run.sh query <args>` - Runs the program with the given arguements
  Changes to argument flags:
      I added two additional flags to denote the database and the table. It was unclear in the prompt whether it could be assumed that the user would always place the database name before the table name, so I created flags to make this explicit. 
      A full command with all flags would look like below :
      `./td-run.sh query -db amazon_security_sample -tb amazon_access -e presto -f csv -l 10 -m 1283212800 -M 1285891200 -c 'target_name, login'`
      
Cleaning the Program 
`./td-run.sh clean`  - Cleans the build directory. 
