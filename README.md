# Vladimir-Nachev-employees
Web API for finding pair of employees which have longest been working together
(potentially they have been working together on different projects).

# Prerequisites
- You should have docker up and running.

# Run the application
- Execute ```docker compose up -d``` in a terminal

# Use the application
You can use the web API in some of the following ways:
1. Access the OpenAPI documentation on the following link: ```http://localhost:8080/swagger-ui.html```
2. Use your favourite API testing tool (for example, Postman)
3. Use ```curl```. Here is an example query:
    * ```curl --location 'localhost:8080/upload-csv-file' --form 'fileName=@"<path_to_your_csv_file_here>"'```

<br/>
<b>Note: The web API supports csv files with no header line containing column names. Here is an example csv content:</b>

143,12,2013-11-01,2014-01-05<br/>
218,10,2012-05-16,NULL<br/>
143,10,2009-01-01,2011-04-27<br/>
218,12,2014-01-02,2014-01-10<br/>
123,1,2023.05.05,2023/05/12<br/>
234,1,2023-05-01,2023.05.05<br/>

<b>It is acceptable for the csv file to contain empty lines and leading whitespaces in front of the lines.</b>

# TODO
- Add global exception handler.
- Add global http exception handler.
- Start using specific code style (for example, Google Code Style).
- Start using linter and include it in the build.
- Start using code coverage tool (for example, SonarCloud).
