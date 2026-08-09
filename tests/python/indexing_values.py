from flask import request

products = [
    {
        "id": 1,
        "name": "Laptop"
    },
    {
        "id": 2,
        "name": "Phone"
    }
]

first_name = products[0]["name"]
last_name = products[-1]["name"]

method = request.method
form_name = request.form["name"]