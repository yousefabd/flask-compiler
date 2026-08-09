age = 24
minimum_age = 18

adult = age >= minimum_age
too_young = age < minimum_age
same_age = age == 24
different_age = age != 25

product = {
    "id": 2,
    "name": "Phone"
}

matching_product = product["id"] == 2
higher_id = product["id"] > 1

empty_products = []
products = [product]

empty_is_false = not empty_products
non_empty_is_false = not products

fallback = "" or "default"
selected = "first" and "second"