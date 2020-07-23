# PI-1920v-L51D-G10

## Repository for developing app COTA for PI course

### Students :

* Tiago Pereira - 43592
* Luis Guerra - 43755

# API Architecture Diagram

![](https://media.discordapp.net/attachments/693837643926208584/727584348043346010/6da5d519e765f0174bceac2606b27b7b.png)

# Requirements
- [NodeJS](https://nodejs.org/)
- [ElasticSearch](https://www.elastic.co/)

# Building and Running the App

- `run bat file of Elastic Search named "elasticsearch.bat"`
- `git clone https://github.com/LuisGuerraa/StolenWorkOfCourse
- `cd COTA`
- `cd client`
- `npm install`
- `npx webpack`
- `cd server (after backing from package client or opening new terminal on PI-1920v-LI52D-G10) `
- `npm install`
- `node cota-server.js`
- `Access localhost (:8080) through your browser`

# Running tests
- `npm install mocha`
- `mocha get-dbtests.js get-webApiTests.js post-dbTests.js`


# Server-side API Documentation

## GET /series/popular/:page
Returns a list of the most popular series. In this case for page=1
```
- Request:
-Body : {}

- Response:
  - Success:
    - Status code: 200
    - Content-Type: application/json
    - Body example:{ "success": {
        "message": "MOST POPULAR SERIES SUCCESSFULLY RETRIEVED",
        "short": "RESOURCE FOUND",
        "data": [
            [
                {
                    "name": "The Flash"
                }
            ],
            [
                {
                    "name": "Law & Order: Special Victims Unit"
                }
            ],
            [
                {
                    "name": "The Simpsons"
                }
            ],
            [
                {
                    "name": "Grey's Anatomy"
                }
            ],
            [
                {
                    "name": "Diriliş: Ertuğrul"
                }
            ],
            [
                {
                    "name": "Supernatural"
                }
            ],
            [
                {
                    "name": "Rick and Morty"
                }
            ],
            [
                {
                    "name": "The 100"
                }
            ],
            [
                {
                    "name": "Breaking Bad"
                }
            ],
            [
                {
                    "name": "The Blacklist"
                }
            ],
            [
                {
                    "name": "Game of Thrones"
                }
            ],
            [
                {
                    "name": "Family Guy"
                }
            ],
            [
                {
                    "name": "Friends"
                }
            ],
            [
                {
                    "name": "White Lines"
                }
            ],
            [
                {
                    "name": "Westworld"
                }
            ],
            [
                {
                    "name": "Blindspot"
                }
            ],
            [
                {
                    "name": "Doctor Who"
                }
            ],
            [
                {
                    "name": "外星女生柴小七"
                }
            ],
            [
                {
                    "name": "Arrow"
                }
            ],
            [
                {
                    "name": "NCIS"
                }
            ]
        ]
    }}

 - Error:
    - Status code: 400
        - Content-Type: application/json
        - Body example: {"ERROR AT FETCHING POPULAR SERIES"}
    - Status code: 503
        - Content-Type: application/json
        - Body example: {"ERROR IN MOVIEDATABASE API"}
    
```

## GET /series/:series_name
Returns series details which contain `series_name` in their name. In this case for 'series_name' = "Homeland"
```
-Request:
-Body : {}

- Response:
  - Success:
    - Status code: 200
    - Content-Type: application/json
    - Body example:{
    "success": {
        "message": "SERIES BREAKING BAD FOUND",
        "short": "RESOURCE FOUND",
        "data": [
            {
                "original_name": "Breaking Bad",
                "genre_ids": [
                    18
                ],
                "name": "Breaking Bad",
                "popularity": 96.274,
                "origin_country": [
                    "US"
                ],
                "vote_count": 4552,
                "first_air_date": "2008-01-20",
                "backdrop_path": "/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg",
                "original_language": "en",
                "id": 1396,
                "vote_average": 8.6,
                "overview": "When Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness and an unrelenting desire to secure his family's financial future at any cost as he enters the dangerous world of drugs and crime.",
                "poster_path": "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg"
            }
        ]
    }
}
- Error:
    - Status code: 404
    - Content-Type: application/json
    - Body example: {"Cannot find (series_name) series"}
    
    - Status code: 503
        - Content-Type: application/json
        - Body example: {"ERROR IN MOVIEDATABASE API"}


```
## POST/groups
Creates group on database
```
- Request:
  - Body: 
      { "name" : 
        "desc" :  
     }
- Response:
  - Success:
    - Status code: 200
    - Content-Type: application/json
    - Body example:{ "Group created" }
  - Error:
     - Status code: 409
          - Body example :{"Duplicate group found"}
     - Status code: 500
          - Body example :{"ERROR IN DB"}

```
## PUT/groups/:group_name
Updates given group on database ( updates group name and description)
```
- Request:
  - Body: 
      { "new_name" : 
        "new_desc" :  
     }
- Response:
  - Success:
    - Status code: 200
    - Content-Type: application/json
    - Body example:{Group (oldgroupName) was updated}
- Error:
    - Status code: 409
         - Content-Type: application/json
         - Body example:{Group (newgroupName) already exists}
    - Status code: 404
         - Content-Type: application/json
         - Body example:{Group (oldgroupName) not found}
    - Status code: 500
          - Body example :{"ERROR IN DB"}
```
## GET/groups
Returns a list of all groups on database with their respective series
```
- Request:
  - Body: none
- Response:
  - Success:
    - Status code: 200
    - Content-Type: application/json
    - Body example:
    ```
"success": {
        "message": " "ALL GROUPS REGISTERED"",
        "short": "RESOURCE FOUND",
        "data": [
            {
[{"name":"Marcio","desc":"descricao muito boa","series":[{"original_name":"Homeland","genre_ids":[80,18,10759,10768],"name":"Homeland","popularity":74.422,"origin_country":["US"],"vote_count":1151,"first_air_date":"2011-10-02","backdrop_path":"/391ixi0wVbFtFLQnOy8ZncWv2io.jpg","original_language":"en","id":1407,"vote_average":7.4,"overview":"CIA officer Carrie Mathison is tops in her field despite being bipolar, which makes her volatile and unpredictable. With the help of her long-time mentor Saul Berenson, Carrie fearlessly risks everything, including her personal well-being and even sanity, at every turn.","poster_path":"/6GAvS2e6VIRsms9FpVt33PsCoEW.jpg"},{"original_name":"Breaking Bad","genre_ids":[18],"name":"Breaking Bad","popularity":103.358,"origin_country":["US"],"vote_count":4261,"first_air_date":"2008-01-20","backdrop_path":"/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg","original_language":"en","id":1396,"vote_average":8.5,"overview":"When Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness and an unrelenting desire to secure his family's financial future at any cost as he enters the dangerous world of drugs and crime.","poster_path":"/ggFHVNu6YYI5L9pCfOacjizRGt.jpg"}]},
{"name":"Luis","desc":"descricao muito boa","series":[]}]
-Error
   - Status code: 500
         - Body example :{"ERROR IN DB"}
   
```
## GET /groups/:group_name
Returns a group's details (name, description and list of series). In this case for 'group_name' = "Marcio"
```
- Request:
  - Body: {}
- Response:
  - Success:
    - Status code: 200
    - Content-Type: application/json
    - Body example:
    ```
"success": {
        "message": " "Group (groupname) found"",
        "short": "RESOURCE FOUND",
        "data": [

[{"name":"Marcio","desc":"descricao muito bom","series":[{"original_name":"Breaking Bad","genre_ids":[18],"name":"Breaking Bad","popularity":103.358,"origin_country":["US"],"vote_count":4261,"first_air_date":"2008-01-20","backdrop_path":"/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg","original_language":"en","id":1396,"vote_average":8.5,"overview":"When Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness and an unrelenting desire to secure his family's financial future at any cost as he enters the dangerous world of drugs and crime.","poster_path":"/ggFHVNu6YYI5L9pCfOacjizRGt.jpg"}]}]

-Error
   - Status code: 500
          - Body example :{"ERROR IN DB"}
   - Status code : 404
          - Body example :{"Group (groupName) not found"}

```
## PUT/groups/:groupName/series
Adds series to the groupName's list of series
```
- Request:
  - Body: {
       seriesName:
}
- Response:
  - Error:
    - Status code :404
        - Body example : { "Group not found" }
 - Status code :404
        - Body example : { "Series not found" }
  - Success:
    - Status code: 200
    - Content-Type: application/json
    - Body example: {"Added (seriesName) to (groupName)}
```
## DELETE /groups/:groupName/series/:seriesName
Deletes seriesName from groupName's list of series
```
- Request:
  - Body: {}
- Response:
  - Error:
    - Status code :404
       - Body example : { "Group not found" }
    - Status code :404
       - Body example : { "Series not found at (groupName)" }
    - Status code: 500
          - Body example :{"ERROR IN DB"}
  - Success:
    - Status code: 200
    - Content-Type: application/json
    - Body example: {"Removed (seriesName) from (groupName)"}
```
## GET /groups/:groupName/series/:min&:max
Returns a sub list of series on groupName's list of series that satisfy vote_average min and max.
```
Example: list of series of Luis's group: 
[{"name":"Luis","desc":"descricao muito boa","series":[{"original_name":"Breaking Bad","genre_ids":[18],"name":"Breaking Bad","popularity":103.358,"origin_country":["US"],"vote_count":4261,"first_air_date":"2008-01-20","backdrop_path":"/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg","original_language":"en","id":1396,"vote_average":8.5,"overview":"When Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness and an unrelenting desire to secure his family's financial future at any cost as he enters the dangerous world of drugs and crime.","poster_path":"/ggFHVNu6YYI5L9pCfOacjizRGt.jpg"},{"original_name":"Siren","genre_ids":[18,10765],"name":"Siren","popularity":58.601,"origin_country":["US"],"vote_count":578,"first_air_date":"2018-03-29","backdrop_path":"/uZNhVUocfi44FuyR3FmHOFLwio4.jpg","original_language":"en","id":71886,"vote_average":7.7,"overview":"The coastal town of Bristol Cove is known for its legend of once being home to mermaids. When the arrival of a mysterious girl proves this folklore all too true, the battle between man and sea takes a very vicious turn as these predatory beings return to reclaim their right to the ocean.","poster_path":"/k906XXqqFMT93v2WMkIOtUcEAlV.jpg"},{"original_name":"Homeland","genre_ids":[80,18,10759,10768],"name":"Homeland","popularity":74.422,"origin_country":["US"],"vote_count":1151,"first_air_date":"2011-10-02","backdrop_path":"/391ixi0wVbFtFLQnOy8ZncWv2io.jpg","original_language":"en","id":1407,"vote_average":7.4,"overview":"CIA officer Carrie Mathison is tops in her field despite being bipolar, which makes her volatile and unpredictable. With the help of her long-time mentor Saul Berenson, Carrie fearlessly risks everything, including her personal well-being and even sanity, at every turn.","poster_path":"/6GAvS2e6VIRsms9FpVt33PsCoEW.jpg"}]}]
```
```
- Request:
  - Body: {}
- Response:
  - Error:
    - Status code :400
       - Body example : { "error":" Interval is not within a possible range" }
    - Status code: 500
          - Body example :{"ERROR IN DB"}

  - Success: ( to use example given we put min = 7.5 and max= 10)
    - Status code: 200
    - Content-Type: application/json
    - Body example:
"success": {
        "message": "SERIES FROM (groupName) VOTES BETWEEN (min) and (max)",
        "short": "RESOURCE FOUND",
        "data": [
[{"original_name":"Breaking Bad","genre_ids":[18],"name":"Breaking Bad","popularity":103.358,"origin_country":["US"],"vote_count":4261,"first_air_date":"2008-01-20","backdrop_path":"/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg","original_language":"en","id":1396,"vote_average":8.5,"overview":"When Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness and an unrelenting desire to secure his family's financial future at any cost as he enters the dangerous world of drugs and crime.","poster_path":"/ggFHVNu6YYI5L9pCfOacjizRGt.jpg"},{"original_name":"Siren","genre_ids":[18,10765],"name":"Siren","popularity":58.601,"origin_country":["US"],"vote_count":578,"first_air_date":"2018-03-29","backdrop_path":"/uZNhVUocfi44FuyR3FmHOFLwio4.jpg","original_language":"en","id":71886,"vote_average":7.7,"overview":"The coastal town of Bristol Cove is known for its legend of once being home to mermaids. When the arrival of a mysterious girl proves this folklore all too true, the battle between man and sea takes a very vicious turn as these predatory beings return to reclaim their right to the ocean.","poster_path":"/k906XXqqFMT93v2WMkIOtUcEAlV.jpg"}]
    
