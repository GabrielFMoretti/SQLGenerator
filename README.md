# Random SQL Generator.

## How it works.

- The program can receive 2 arguments, the first is mandatory, is the number of rows to generate, the second one is
  the `.json` file that describe the tables, like the sql create instruction. If this second parameter was never pass
  the program will always look to the root folder and search for a file named `tables.json` to consider the tables, if
  not found throws Exception.
- After that the sql inserts will be print in console.

## How create `tables.json` file

The tables file describe the tables that you wanna to generate insert querys, so it looks like a instruction to create
table. Here is a sample (all fields are mandatory):

    ```
     [
        {
            "table": "users",
            "fields": [
                {
                    "nameField": "name",
                    "faker": "firstName",
                    "type": "STRING",
                    "methodParams": []
                },
                {
                    "nameField": "email",
                    "faker": "emailAddress",
                    "type": "STRING",
                    "methodParams": []
                },
                {
                    "nameField": "age",
                    "faker": "numberBetween",
                    "type": "INTEGER",
                    "methodParams": [
                        {
                            "className": "int",
                            "value": "1"
                        },
                        {
                            "className": "int",
                            "value": "100"
                        }
                    ]
                }
            ]
        }
    ]
    ```

The `faker` field is what determine the random value that will be create. To know all the types of values that can be
generate check de [configuration.json file](./src/main/resources/configuration.json).

### Relationships

The program can generate random values from relationships too, all that must be done is in the `tables.json file` put
first the tables that don't have relationship and after the tables that have, like in sql, the tables without a
relationship came first. After knowing that is only put the option field `relationship` in the object of table.

Sample:

```
     [
      \\ ... tables usuarios above
        {
            "table": "users_address",
            "fields": [
                {
                    "nameField": "street",
                    "faker": "streetAddress",
                    "type": "STRING",
                    "methodParams": []
                },
                {
                    "nameField": "city",
                    "faker": "city",
                    "type": "STRING",
                    "methodParams": []
                },
                {
                    "nameField": "user_email",
                    "faker": "emailAddress",
                    "type": "STRING",
                    "methodParams": [],
                    "relationship": {
                          "table": "users",
                          "field": "email"
                    }
                }
            ]
        }
    ]
    ```
