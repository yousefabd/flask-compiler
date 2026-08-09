products = [
    {"id": 1, "name": "Laptop"},
    {"id": 2, "name": "Phone"},
    {"id": 3, "name": "Headphones"}
]


def find_product_by_id(product_id):
    for product in products:
        if product["id"] == product_id:
            return product

    return None


def get_max_product_id():
    max_id = 0

    for product in products:
        if product["id"] > max_id:
            max_id = product["id"]

    return max_id


found_product = find_product_by_id(2)
missing_product = find_product_by_id(99)
found_name = found_product["name"]

maximum_id = get_max_product_id()


pairs = [
    ["first", 10],
    ["second", 20]
]

pair_total = 0
pair_names = ""

for name, value in pairs:
    pair_names = pair_names + name
    pair_total = pair_total + value


dictionary = {
    "a": 1,
    "b": 2
}

dictionary_keys = ""

for key in dictionary:
    dictionary_keys = dictionary_keys + key


empty_status = "before"

for item in []:
    empty_status = "body"
else:
    empty_status = "empty"