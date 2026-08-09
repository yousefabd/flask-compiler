products = [
    {"id": 1, "name": "Laptop"},
    {"id": 2, "name": "Phone"}
]

status = "before"
count = 0
label = "module"


def replace_products():
    global products, status

    products = [
        {"id": 99, "name": "Replacement"}
    ]

    status = "replaced"
    local_only = "inside"


def increment():
    global count
    count = count + 1


def shadow_label():
    label = "local"
    return label


replace_products()

increment()
increment()

shadow_result = shadow_label()

first_product_id = products[0]["id"]
first_product_name = products[0]["name"]