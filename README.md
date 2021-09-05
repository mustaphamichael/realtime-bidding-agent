# Real-time Bidding Agent

## Run application
- Run `sbt run` to start the application (app runs on PORT 8080 by default).

To run the application using a different dataset, update the data in `src/main/scala/com/rtb/data/CampaignData#activeCampaigns`. Ensure to restart the application after changing the data.

## Tests

#### Unit test
- Run `sbt test` to run unit tests.

#### HTTP test
- Send a POST request to the endpoint `http://localhost:8080/make-bid` with a valid BidRequest as the body.

  Example using cURL
```
HTTP Request

curl --location --request POST 'http://localhost:8080/make-bid' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": "REQ001",
    "site": { "id": "0006a522ce0f4bbbbaa6b3c38cafaa0f", "domain": "fake.tld" },
    "device": { "id": "440579f4b408831516ebd02f6e1c31b4", "geo": { "country": "LT" } },
    "imp": [
        { "id": "1", "wmin": 50, "wmax": 300, "hmin": 100, "hmax": 300, "h": 250, "w": 300, "bidFloor": 3.12123 }
    ],
    "user": { "id": "USARIO1", "geo": { "country": "LT" } }
}'
```

The service returns a **200 OK** response if a matched bid exists.
```
HTTP Response

{
    "id": "c59af3a02c3941a3a838d470668798c4",
    "bidRequestId": "REQ001",
    "price": 3.12123,
    "adid": "1",
    "banner": {
        "id": 1,
        "src": "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
        "width": 300,
        "height": 250
    }
}
```

OR

returns a **HTTP 204: No Content** response if there is no matched bid.

