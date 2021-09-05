# Real-time Bidding Agent

A real-time bidding agent is a simple HTTP server that accepts JSON requests, does some matching between advertising
campaigns and the received bid request and responds with either a JSON response with a matched campaign (bid) or an
empty response (no bid).

## Scala
The project was developed using `Scala 2.13.6` and `sbt 1.4.3`.

## Clone
```
$ git clone https://github.com/mustaphamichael/realtime-bidding-agent.git
$ cd realtime-bidding-agent
```

## Run application

- Run `sbt run` to start the application (runs on PORT 8080 by default).

To run the application using a different dataset, update the data
in `src/main/scala/com/rtb/data/CampaignData#activeCampaigns`. Ensure to restart the application after changing the
data.

## Tests

#### Unit test

- Run `sbt test` to run unit tests.

#### HTTP test

- Send a POST request to the endpoint `http://localhost:8080/make-bid` with a valid BidRequest as the body while the
  application is running.

The service returns a **200 OK** response with a BidResponse JSON payload if a matched bid exists, else returns a **204
No Content** response if there is no matched bid.

## Assumption

- Only the `targetedSiteIds` is used as a matching criteria for the Targeting option.
