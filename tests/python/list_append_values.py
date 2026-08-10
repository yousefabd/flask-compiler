products = [
    {"id": 1, "name": "Laptop"},
    {"id": 2, "name": "Phone"}
]


def add_product(product):
    return products.append(product)


def remove_product_by_id(product_id):
    global products

    new_products = []

    for product in products:
        if product["id"] != product_id:
            new_products.append(product)

    products = new_products


append_result = add_product(
    {"id": 3, "name": "Keyboard"}
)

remove_product_by_id(1)