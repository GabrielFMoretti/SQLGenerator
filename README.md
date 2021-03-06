# Random SQL Generator.

## How it works.

- The program can receive 2 arguments, the first is mandatory, is the number of rows to generate, the second one is
  the `.json` file that describe the tables, like the sql create instruction. If this second parameter was never pass
  the program will always look to the root folder and search for a file named `tables.json` to consider the tables, if
  not found throws Exception.
- After that the sql inserts will be print in console and save in a file named
  `result.sql` in the root folder.

## How create `tables.json` file

The tables file describe the tables that you wanna to generate insert querys,
so it looks like a sql instruction to create table.
Here is a sample (the fields `unique`and `relationship` are optionals) :

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

The option of relation can be by one field of another table or considering an auto-
increment id generation. In the sample we have both.

Sample:

```
     [
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
        },
        {
            "table": "installations_relationship_sample",
            "fields": [
                {
                    "nameField": "city",
                    "faker": "city",
                    "type": "STRING",
                    "methodParams": []
                },
                {
                    "nameField": "installtion_id",
                    "faker": "number",
                    "type": "INTEGER",
                    "methodParams": [],
                    "relationship": {
                          "table": "installation",
                          "isAutoGenerateID": true
                    }
                }
            ]
        }
    ]
    
```
