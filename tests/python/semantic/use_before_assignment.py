module_copy = module_value
module_value = 10

def broken():
    local_copy = local_value
    local_value = 20
    return local_copy

counter = counter + 1

def valid_forward_global():
    return declared_later

declared_later = 30