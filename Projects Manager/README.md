# Project Issue Management 

### About 
* DAW-LI61N - Group 03
* 2019-2020 Summer Semester
* Repository for developing a project for DAW course 

### Documentation 
* [API Documentation](https://github.com/isel-leic-daw/S1920V-LI61N-G03/blob/master/docs/API_Documentation.pdf)

* [Json Home of the API](https://github.com/isel-leic-daw/S1920V-LI61N-G03/wiki/API-Json-Home-Documentation)

### Architecture

<p align="center">
  <img src="https://i.gyazo.com/2ba17007d950f6cff84545bdaeaeaf73.png" />
</p>

### Technologies used :

For the server-side we use [Kotlin](https://kotlinlang.org/) language implemented through [Spring](https://spring.io/) framework, particularly [SpringMVC](https://spring.io/guides/gs/serving-web-content/). On the client-side we choose to build the user interface with JavaScript through a library called [React](https://reactjs.org/).

### Database Entity Diagram

<p align="center">
  <img src="https://github.com/isel-leic-daw/S1920V-LI61N-G03/blob/master/Backend/resources/DatabaseFiles/EAModel_DAW.png" alt="Entity-Association DB Model"/>
</p>

### Brief Information about the database:

Our database is implemented on an open source object-relational database system called [PostgresSQL](https://www.postgresql.org/) in which we use [this](https://github.com/isel-leic-daw/S1920V-LI61N-G03/blob/master/Backend/resources/DatabaseFiles/DBModel.sql) script to build our physical model. To see more documentation about PostegresSql click [here](https://www.postgresql.org/docs/manuals/archive/). The interaction between the backend and the database is done using [JDBC](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/jdbc/core/package-summary.html) API.

