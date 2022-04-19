
README.me

# Currency Exchange REST api application

API is supporting current and historical exchange rates data.


## API Reference examples

#### Get current exchange rate for base and target

```http
  GET /api/current/{base}/{target}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `base`    | `string` | **Required** base          |
| `target`  | `string` | **Required** target        |

#### Example
```http
http://localhost:8080/api/current/PLN/GBP
```
#### JSON Result
```json
{
"base":"PLN",
"target":"GBP",
"rate":0.1785,
"date":"2022-04-15"
}
```

#### Get historical exchange rate for a specific date

```http
  GET /api/historic/{base}/{target}/{date}
```

| Parameter | Type     | Description                   |
| :-------- | :------- | :-----------------------------|
| `base`    | `string` | **Required**. base            |
| `target`  | `string` | **Required**. target          |
| `date`    | `string` | **Required**. date            |

#### Example
```http
http://localhost:8080/api/historic/USD/GBP/2022-02-02
```
#### JSON Result
```json
{
"base":"USD",
"target":"GBP",
"rate":0.7367,
"date":"2022-02-22"
}
```

#### Get historical exchange rate for a range of dates

```http
  GET /api/between/{base}/{target}/{start}/{end}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `base`    | `string` | **Required**. base                |
| `target`  | `string` | **Required**. target              |
| `start`   | `string` | **Required**. date                |
| `end`     | `string` | **Required**. date                |

#### Example
```http
http://localhost:8080/api/between/USD/GBP/2022-02-23/2022-02-25
```
#### JSON Result
```json
[
    {
    "base":"USD",
     "target":"GBP",
     "rate":0.7393,
     "date":"2022-02-23"
     },

    {
    "base":"USD",
    "target":"GBP",
    "rate":0.7477,
    "date":"2022-02-24"
    },

    {
    "base":"USD",
    "target":"GBP",
    "rate":0.7459,
    "date":"2022-02-25"
    }
]


## Used Tech Stack

Technnologies: Java, Spring Framework, database: PostgresSQL, Swagger


## Authors

- [@valerivoinarovych](hhttps://github.com/vvoinarovych)
- [@lukaszlewandowski](https://github.com/ukas87)

